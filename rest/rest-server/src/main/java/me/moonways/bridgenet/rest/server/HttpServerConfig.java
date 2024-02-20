package me.moonways.bridgenet.rest.server;

import lombok.*;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.server.controller.HttpContextPattern;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class HttpServerConfig {

    private final HttpHost host;
    private final HttpCredentials credentials;

    private final Map<String, HttpContextPattern> controllerPatternsMap = new HashMap<>();

    public HttpContextPattern find(String method, String name) {
        return controllerPatternsMap.values()
                .stream()
                .filter(pattern -> pattern.getMethod() == null || Objects.equals(method, pattern.getMethod()))
                .filter(pattern -> pattern.getName().equals(name))
                .findFirst()
                .orElse(null);
    }
}
