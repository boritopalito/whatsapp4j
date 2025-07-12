package nl.xx1.whatsapp4j.auth;

import com.microsoft.playwright.BrowserContext;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalAuth extends AuthStrategy {

    @Override
    public Path beforeBrowser() {
        if (!Files.exists(Path.of("storage.json"))) {
            try {
                Files.createFile(Path.of("storage.json"));
            } catch (IOException e) {
//                throw new RuntimeException(e);
            }
        }

        return Path.of("storage.json");
    }

    @Override
    public void afterBrowser() {

    }

    @Override
    public void onSuccessfulLogin() {
        System.out.println("Saving login.");
        this.client.getBrowserContext().storageState(new BrowserContext.StorageStateOptions().setPath(Paths.get("storage.json")));
    }

}
