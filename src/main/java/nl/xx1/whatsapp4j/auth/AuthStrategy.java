package nl.xx1.whatsapp4j.auth;

import java.nio.file.Path;
import nl.xx1.whatsapp4j.Client;

public abstract class AuthStrategy {
    protected Client client;

    public void setup(Client client) {
        this.client = client;
    }

    public abstract Path beforeBrowser();

    public abstract void afterBrowser();

    public abstract void onSuccessfulLogin();
}
