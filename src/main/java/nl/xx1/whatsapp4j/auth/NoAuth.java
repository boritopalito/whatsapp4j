package nl.xx1.whatsapp4j.auth;

import java.nio.file.Path;

public class NoAuth extends AuthStrategy {
    @Override
    public Path beforeBrowser() {
        return null;
    }

    @Override
    public void afterBrowser() {}

    @Override
    public void onSuccessfulLogin() {}
}
