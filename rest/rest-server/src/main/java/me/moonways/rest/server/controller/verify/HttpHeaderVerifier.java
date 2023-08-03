package me.moonways.rest.server.controller.verify;

import me.moonways.rest.api.HttpHeader;
import org.jetbrains.annotations.NotNull;

public interface HttpHeaderVerifier {

    boolean matchesInternal(@NotNull HttpHeader httpHeader);

    boolean confirm(@NotNull HttpHeader httpHeader, @NotNull VerificationConfig verificationConfig);
}
