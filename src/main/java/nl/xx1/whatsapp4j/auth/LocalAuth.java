package nl.xx1.whatsapp4j.auth;

import java.nio.file.Path;

public class LocalAuth extends AuthStrategy {

    private Path getPath() {
        return Path.of("w4j_auth", this.client.getOptions().session());
    }

    @Override
    public Path beforeBrowser() {
        return getPath();
    }

    @Override
    public void afterBrowser() {}

    @Override
    public void onSuccessfulLogin() {}
}
