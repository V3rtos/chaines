package me.moonways.bridgenet.rest4j;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder
@EqualsAndHashCode
@RequiredArgsConstructor
public final class Bridgenet4jConfig {

    private final String baseurl;

    private final String apiKey;

    private final Integer connectTimeout;
    private final Integer readTimeout;
}
