package nl.xx1.whatsapp4j;

import nl.xx1.whatsapp4j.auth.AuthStrategy;
import nl.xx1.whatsapp4j.auth.NoAuth;

public record ClientLaunchOptions(AuthStrategy authStrategy) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private AuthStrategy authStrategy = new NoAuth();

        public Builder() {}

        public Builder authStrategy(AuthStrategy authStrategy) {
            this.authStrategy = authStrategy;
            return this;
        }

        public ClientLaunchOptions build() {
            return new ClientLaunchOptions(authStrategy);
        }
    }
}
