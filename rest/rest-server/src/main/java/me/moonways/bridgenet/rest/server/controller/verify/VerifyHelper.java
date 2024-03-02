package me.moonways.bridgenet.rest.server.controller.verify;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rest.api.HttpHeader;
import me.moonways.bridgenet.rest.api.exception.RestVerifyException;
import me.moonways.bridgenet.rest.server.HttpServerConfig;
import me.moonways.bridgenet.rest.server.controller.verify.type.AuthenticationPasswordVerifier;
import me.moonways.bridgenet.rest.server.controller.verify.type.AuthenticationUsernameVerifier;
import me.moonways.bridgenet.rest.server.controller.verify.type.KeyVerifier;
import org.apache.http.Header;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Log4j2
@RequiredArgsConstructor
public class VerifyHelper {

    private final HttpServerConfig config;
    private final EnumMap<VerifyType, HttpHeaderVerifier> verifiersMap = new EnumMap<>(VerifyType.class);

    public VerificationConfig process(HttpRequest request, HttpResponse response) {
        final VerificationConfig verificationConfig = new VerificationConfig();

        final Consumer<HttpHeader> action = (element) ->
                verifiersMap.forEach((verifyType, verifier) -> {

                    if (verifier.matchesInternal(element)) {
                        if (!verifier.confirm(element, verificationConfig)) {
                            log.error(new RestVerifyException(verifyType.name()));
                        }
                    }
                });

        final Stream<Header> requestHeadersStream = Stream.of(request.getAllHeaders());
        final Stream<Header> responseHeadersStream = Stream.of(response.getAllHeaders());
        Stream.concat(requestHeadersStream, responseHeadersStream)
                .flatMap(header ->
                        Stream.of(header.getElements())
                                .map(element -> new HttpHeader(header.getName(), element.getName())))
                .forEachOrdered(action);

        return verificationConfig;
    }

    public void initVerifiers() {
        if (!verifiersMap.isEmpty()) {
            return;
        }

        @AllArgsConstructor
        class LocalVerifier {

            private VerifyType type;
            private HttpHeaderVerifier verifier;
        }

        final LocalVerifier[] verifiers = {
                new LocalVerifier(VerifyType.AUTHENTICATION_USERNAME, new AuthenticationUsernameVerifier(config)),
                new LocalVerifier(VerifyType.AUTHENTICATION_PASSWORD, new AuthenticationPasswordVerifier(config)),
                new LocalVerifier(VerifyType.APIKEY, new KeyVerifier(config)),
        };

        Map<VerifyType, HttpHeaderVerifier> collectMap = Stream.of(verifiers)
                .collect(
                        Collectors.toMap(
                                localVerifier -> localVerifier.type,
                                localVerifier -> localVerifier.verifier
                        ));

        verifiersMap.putAll(collectMap);
    }
}
