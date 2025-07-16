package nl.xx1.whatsapp4j.utils;

import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import java.util.function.Function;

public class ExposeFunctionUtil {
    public static <T> void exposeFunctionIfAbsent(Page page, String name, Function<T, Object> fn) {
        BrowserContext context = page.context();

        boolean exist = (boolean) page.evaluate("name => !!window[name]", name);

        if (exist) {
            return;
        }

        context.exposeBinding(name, (source, args) -> fn.apply((T) args[0]));
    }
}
