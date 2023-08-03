package me.moonways.rest.api.exchange.response;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.moonways.rest.api.exchange.message.RestMessageType;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestResponseBuilder {

    public static RestResponseBuilder create() {
        return new RestResponseBuilder()
                .resetDefaults();
    }

    private int statusCode;

    private String method;
    private String textContent;

    public RestResponseBuilder setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public RestResponseBuilder setMethod(String method) {
        this.method = method;
        return this;
    }

    public RestResponseBuilder setTextContent(String textContent) {
        this.textContent = textContent;
        return this;
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @SneakyThrows
    public RestResponseBuilder setInputContent(InputStream inputStream) {
        byte[] contentBytes = new byte[inputStream.available()];
        inputStream.read(contentBytes);
        return setTextContent(new String(contentBytes, StandardCharsets.UTF_8));
    }

    public RestResponse build() {
        return new RestResponse(statusCode, method, textContent);
    }

    private RestResponseBuilder resetDefaults() {
        statusCode = HttpURLConnection.HTTP_OK;
        method = RestMessageType.GET.name();
        return this;
    }
}
