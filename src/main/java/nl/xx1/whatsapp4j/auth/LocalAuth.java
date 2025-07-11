package nl.xx1.whatsapp4j.auth;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.options.Cookie;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class LocalAuth extends AuthStrategy {

    @Override
    public Path beforeBrowser() {

        if (!Files.exists(Path.of("session.json"))) {
            try {
                Files.createFile(Path.of("session.json"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return Path.of("session.json");
    }

    @Override
    public void onSuccessfulLogin() {
        BrowserContext context = this.client.getBrowserContext();
        List<Cookie> cookies = context.cookies();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = Files.newBufferedWriter(Paths.get("cookies.json"))) {
            gson.toJson(cookies, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
