package nl.xx1.whatsapp4j.auth;

import nl.xx1.whatsapp4j.Client;

import java.nio.file.Path;

public abstract class AuthStrategy {

    protected Client client;

    public void setup(Client client) {
        this.client = client;
    }

    public abstract Path beforeBrowser();

    public abstract void onSuccessfulLogin();
}
