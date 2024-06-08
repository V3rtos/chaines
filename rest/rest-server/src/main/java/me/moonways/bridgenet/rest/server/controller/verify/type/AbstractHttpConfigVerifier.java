package me.moonways.bridgenet.rest.server.controller.verify.type;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.api.HttpHeader;
import me.moonways.bridgenet.rest.server.HttpServerConfig;
import me.moonways.bridgenet.rest.server.controller.verify.HttpHeaderVerifier;
import me.moonways.bridgenet.rest.server.controller.verify.VerificationConfig;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public abstract class AbstractHttpConfigVerifier implements HttpHeaderVerifier {

    private final HttpServerConfig config;
    private final String elementName;

    protected abstract String lookupCorrectly(HttpServerConfig config);

    @Override
    public final boolean matchesInternal(@NotNull HttpHeader httpHeader) {
        return httpHeader.getName().equals(elementName);
    }

    @Override
    public final boolean confirm(@NotNull HttpHeader httpHeader, @NotNull VerificationConfig verificationConfig) {
        final String correctly = lookupCorrectly(config);

        boolean confirmed = httpHeader.getValue().equals(correctly);
        postVerify(verificationConfig, confirmed);

        return confirmed;
    }

    protected void postVerify(VerificationConfig verificationConfig, boolean confirmed) {
        // override me.
    }
}
