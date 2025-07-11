package nl.xx1.whatsapp4j;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.LoadState;
import nl.xx1.whatsapp4j.utils.JsUtils;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;

import static nl.xx1.whatsapp4j.utils.ExposeFunctionUtil.exposeFunctionIfAbsent;

public class Client {
    private static final String WHATSAPP_URL = "https://web.whatsapp.com/";

    private final Map<Event, List<ClientEventListener<?>>> listeners = new EnumMap<>(Event.class);
    private final CountDownLatch closeLatch = new CountDownLatch(1);

    private Browser browser;
    private Page page;

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
        browser = Playwright.create().chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        page = browser.newPage(new Browser.NewPageOptions().setBypassCSP(true));

        page.navigate(WHATSAPP_URL);
        page.waitForLoadState(LoadState.LOAD);

        this.inject();

        this.page.onFrameNavigated(frame -> {
            this.inject();
        });

        while (true) {
            this.page.waitForTimeout(1);
        }
    }

    private void inject() {
        this.page.evaluate(JsUtils.loadJsFromResources("js/auth_store.js"));

        if (!isAuthenticated()) {
            exposeFunctionIfAbsent(page, "onQRChangedEvent", arg -> {
                fireEvent(Event.QR_READY, arg);
                return "result";
            });

            this.page.evaluate(JsUtils.loadJsFromResources("js/inject_qr.js"));
        }
    }

    private boolean isAuthenticated() {
        AppState appState = getAppState();

        while (AppState.getOpeningStates().contains(appState)) {
            appState = getAppState();
        }

        return !AppState.getUnpairedStates().contains(appState);
    }

    public AppState getAppState() {
        String appState = (String) this.page.evaluate("window.AuthStore.AppState.state");
        return AppState.valueOf(appState);
    }

    public void waitForClose() throws InterruptedException {
        closeLatch.await();
    }
}
