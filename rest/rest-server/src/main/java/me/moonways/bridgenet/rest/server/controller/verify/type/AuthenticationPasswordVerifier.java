package me.moonways.bridgenet.rest.server.controller.verify.type;

import me.moonways.bridgenet.rest.api.StandardHeaders;
import me.moonways.bridgenet.rest.server.controller.verify.VerificationConfig;
import me.moonways.bridgenet.rest.server.HttpServerConfig;

public final class AuthenticationPasswordVerifier extends AbstractHttpConfigVerifier {

    public AuthenticationPasswordVerifier(HttpServerConfig config) {
        super(config, StandardHeaders.Key.BRIDGENET_PASSWORD);
    }

    @Override
    protected String lookupCorrectly(HttpServerConfig config) {
        return config.getCredentials().getPassword();
    }

    @Override
    protected void postVerify(VerificationConfig verificationConfig, boolean confirmed) {
        if (confirmed) {
            verificationConfig.increaseCredentialsVerify();
        }
    }
}
