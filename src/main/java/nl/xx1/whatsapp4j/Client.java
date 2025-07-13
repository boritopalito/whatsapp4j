package nl.xx1.whatsapp4j;

import com.microsoft.playwright.*;
import com.microsoft.playwright.options.LoadState;
import nl.xx1.whatsapp4j.auth.NoAuth;
import nl.xx1.whatsapp4j.utils.JsUtils;
import nl.xx1.whatsapp4j.webcache.WebCache;
import nl.xx1.whatsapp4j.webcache.WebCacheFactory;

import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static nl.xx1.whatsapp4j.utils.ExposeFunctionUtil.exposeFunctionIfAbsent;

public class Client {
    private static final String WHATSAPP_URL = "https://web.whatsapp.com/";


    private final Map<Event, List<ClientEventListener<?>>> listeners = new EnumMap<>(Event.class);
    private final CountDownLatch closeLatch = new CountDownLatch(1);

    private String currentIndexHtml = "";

    private BrowserContext browserContext;
    private Page page;

    private final ClientLaunchOptions options;

    public Client() {
        this(ClientLaunchOptions.builder().build());
    }

    public Client(ClientLaunchOptions options) {
        this.options = options;
    }

    public ClientLaunchOptions getOptions() {
        return this.options;
    }

    public <T> void on(Event event, ClientEventListener<T> listener) {
        listeners.computeIfAbsent(event, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    public <T> void off(Event event, ClientEventListener<T> listener) {
        List<ClientEventListener<?>> eventListeners = listeners.get(event);
        if (eventListeners != null) eventListeners.remove(listener);
    }

    @SuppressWarnings("unchecked")
    public <T> void fireEvent(Event event, T arg) {
        List<ClientEventListener<?>> eventListeners = listeners.get(event);

        if (eventListeners == null) {
            return;
        }

        for (ClientEventListener<?> listener : eventListeners) {
            ((ClientEventListener<T>) listener).onEvent(arg);
        }
    }

    public void start() {
        options.authStrategy().setup(this);

        if (options.authStrategy() instanceof NoAuth) {
            Browser browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
            browserContext = browser.newContext();
        } else {
            Path userDataDir = options.authStrategy().beforeBrowser();
            browserContext = Playwright.create().chromium().launchPersistentContext(userDataDir, new BrowserType.LaunchPersistentContextOptions().setBypassCSP(true).setHeadless(false));
        }

        page = browserContext.newPage();

        this.options.authStrategy().afterBrowser();
        this.initWebVersionCache();

        page.navigate(WHATSAPP_URL);
        page.waitForLoadState(LoadState.LOAD);

        this.inject();

        this.page.onFrameNavigated(frame -> {
            System.out.println(frame.hashCode());
            this.inject();
        });

        while (true) {
            this.page.waitForTimeout(1);
        }
    }

    private void initWebVersionCache() {
        WebCache webCache = WebCacheFactory.create("local");
        Optional<String> optional = webCache.resolve("1.0.0");

        if (optional.isEmpty()) {
            this.page.onResponse(response -> {
                if (!response.ok() || !response.url().equals(WHATSAPP_URL)) {
                    return;
                }

                currentIndexHtml = response.text();
            });
            return;
        }

        this.page.route(WHATSAPP_URL, route -> {
            route.fulfill(
                    new Route.FulfillOptions()
                            .setBody(optional.get())
                            .setContentType("text/html")
                            .setStatus(200)
            );
        });
    }

    private void inject() {
        this.page.waitForFunction("window.Debug?.VERSION != undefined");

        this.page.evaluate(JsUtils.loadJsFromResources("js/auth_store.js"));

        this.page.waitForFunction("window.AuthStore != undefined");

        if (!isAuthenticated()) {
            exposeFunctionIfAbsent(page, "onQRChangedEvent", arg -> {
                fireEvent(Event.QR_READY, arg);
                return "result";
            });

            this.page.evaluate(JsUtils.loadJsFromResources("js/inject_qr.js"));
        }

        exposeFunctionIfAbsent(page, "onAppStateHasSyncedEvent", arg -> {
            if (!this.currentIndexHtml.isEmpty()) {
                WebCache webCache = WebCacheFactory.create("local");
                webCache.persist(this.currentIndexHtml, "1.0.0");
            }

            this.options.authStrategy().onSuccessfulLogin();
            this.fireEvent(Event.READY, null);
            return "result";
        });

        this.page.evaluate("""
           () => {
               window.AuthStore.AppState.on('change:hasSynced', () => {
                    console.log("change:hasSynced tiggered");
                    window.onAppStateHasSyncedEvent(1);
                });
           }
        """);
    }

    private boolean isAuthenticated() {
        AppState appState = getAppState();

        while (AppState.getOpeningStates().contains(appState)) {
            appState = getAppState();
        }

        return !AppState.getUnpairedStates().contains(appState);
    }

    public AppState getAppState() {
        try {
            String appState = (String) this.page.evaluate("window.AuthStore.AppState.state");
            return AppState.valueOf(appState);
        } catch (PlaywrightException e) {
            return AppState.UNLAUNCHED;
        }
    }

    public void waitForClose() throws InterruptedException {
        closeLatch.await();
    }
}
