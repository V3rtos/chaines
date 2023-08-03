package me.moonways.rest.server.controller.verify.type;

import lombok.RequiredArgsConstructor;
import me.moonways.rest.api.HttpHeader;
import me.moonways.rest.api.StandardHeaders;
import me.moonways.rest.server.HttpServerConfig;
import me.moonways.rest.server.controller.verify.HttpHeaderVerifier;
import me.moonways.rest.server.controller.verify.VerificationConfig;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class KeyVerifier implements HttpHeaderVerifier {

    private static final String PUBLIC = "mwr3stjutIh9B5tmEzh7BO0yalqDkbAClGbOQ2l8oRJjfybTm0mJ3v9v7jBiJcVWt9kT5jLqMBZwpyhxtBJd8AY3BIe5f84ir4BM8KMyKQ7G6JQRGFIrZO5qjYjEcwTh";
    private static final String PRIVATE = "mwr3stTsXHpTsti4QClKKnCgRH90wjauv4FbgWSOk9g1N4YVXVaII3U209jG6MtBhm7weoOgNszIOTT9SYapYg71bIS2fxlYRplAFoq6nMEVUuLtZLWvRPjyOHLlMZBe";

    private final HttpServerConfig config;

    @Override
    public boolean matchesInternal(@NotNull HttpHeader httpHeader) {
        return httpHeader.getName().equals(StandardHeaders.Key.BRIDGENET_APIKEY);
    }

    @Override
    public boolean confirm(@NotNull HttpHeader httpHeader, @NotNull VerificationConfig verificationConfig) {
        switch (httpHeader.getValue()) {
            case PUBLIC:
                verificationConfig.setPublic(true);
            case PRIVATE:
                verificationConfig.setVerified(true);
                return true;
        }
        return false;
    }
}
