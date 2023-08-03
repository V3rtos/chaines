package me.moonways.rest.api.exchange.message;

import lombok.*;
import me.moonways.rest.api.exchange.entity.ExchangeableEntity;

import java.util.Properties;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class RestMessage {

    private final Properties headers;
    private final Properties urlParameters;

    private final String context;

    private final RestMessageType type;
    private final ExchangeableEntity entity;
}
