package me.moonways.rest.server.controller.verify.type;

import me.moonways.rest.api.StandardHeaders;
import me.moonways.rest.server.HttpServerConfig;
import me.moonways.rest.server.controller.verify.VerificationConfig;

public final class AuthenticationUsernameVerifier extends AbstractHttpConfigVerifier {

    public AuthenticationUsernameVerifier(HttpServerConfig config) {
        super(config, StandardHeaders.Key.BRIDGENET_USERNAME);
    }

    @Override
    protected String lookupCorrectly(HttpServerConfig config) {
        return config.getCredentials().getUsername();
    }

    @Override
    protected void postVerify(VerificationConfig verificationConfig, boolean confirmed) {
        if (confirmed) {
            verificationConfig.increaseCredentialsVerify();
        }
    }
}
