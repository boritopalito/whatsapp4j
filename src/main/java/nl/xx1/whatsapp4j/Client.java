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

import static nl.xx1.whatsapp4j.utils.ExposeFunctionUtil.exposeFunctionIfAbsent;

public class Client {
    private static final String WHATSAPP_URL = "https://web.whatsapp.com/";

    private final Map<Event, List<ClientEventListener<?>>> listeners = new EnumMap<>(Event.class);

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
        page = browser.newPage();

        page.navigate(WHATSAPP_URL);
        page.waitForLoadState(LoadState.LOAD);

        this.inject();
    }

    private void inject() {
        this.page.evaluate(JsUtils.loadJsFromResources("js/auth_store.js"));

        boolean needAuthentication = (boolean) page.evaluate(JsUtils.loadJsFromResources("js/auth_status.js"));

        if (needAuthentication) {
            exposeFunctionIfAbsent(page, "onQRChangedEvent", arg -> {
                fireEvent(Event.QR_READY, arg);
                return "result";
            });

            this.page.evaluate(JsUtils.loadJsFromResources("js/inject_qr.js"));
        }
    }
}
