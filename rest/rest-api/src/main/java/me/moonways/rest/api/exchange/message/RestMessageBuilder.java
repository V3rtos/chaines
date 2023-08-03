package me.moonways.rest.api.exchange.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.moonways.rest.api.exchange.entity.ExchangeableEntity;

import java.util.Properties;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestMessageBuilder {

    public static RestMessageBuilder create() {
        return new RestMessageBuilder()
                .resetDefaults();
    }

    private final Properties headers = new Properties();
    private final Properties urlParameters = new Properties();

    private String context;

    private RestMessageType type;
    private ExchangeableEntity entity;

    public RestMessageBuilder addHeader(String header, String value) {
        headers.setProperty(header, value);
        return this;
    }

    public RestMessageBuilder addParameter(String name, String value) {
        urlParameters.setProperty(name, value);
        return this;
    }

    public RestMessageBuilder setContext(String context) {
        this.context = context;
        return this;
    }

    public RestMessageBuilder setType(RestMessageType type) {
        this.type = type;
        return this;
    }

    public RestMessageBuilder setEntity(ExchangeableEntity entity) {
        this.entity = entity;
        return this;
    }

    public RestMessage build() {
        return new RestMessage(headers, urlParameters, context, type, entity);
    }

    private RestMessageBuilder resetDefaults() {
        context = "/";
        type = RestMessageType.GET;
        return this;
    }
}
