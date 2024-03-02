package me.moonways.bridgenet.rest.api.exchange.response;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RestResponse {

    private final int statusCode;

    private final String method;
    private final String responseContent;
}
