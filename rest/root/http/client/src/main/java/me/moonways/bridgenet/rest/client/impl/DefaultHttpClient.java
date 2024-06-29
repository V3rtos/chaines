package me.moonways.bridgenet.rest.client.impl;

import me.moonways.bridgenet.rest.client.ClientHttpRequest;
import me.moonways.bridgenet.rest.client.request.URLClientRequest;
import me.moonways.bridgenet.rest.model.HttpRequest;
import lombok.Builder;

import java.util.concurrent.ExecutorService;

public class DefaultHttpClient extends AbstractHttpClient {

    private static final long serialVersionUID = 2314876142908419141L;

    private final Integer connectTimeout;
    private final Integer readTimeout;

    @Builder
    protected DefaultHttpClient(ExecutorService executorService, Integer connectTimeout, Integer readTimeout) {
        super(executorService);
        this.connectTimeout = connectTimeout;
        this.readTimeout = readTimeout;
    }

    @Override
    public ClientHttpRequest create(HttpRequest httpRequest) {
        return URLClientRequest.builder()
                .connectTimeout(connectTimeout)
                .readTimeout(readTimeout)
                .executorService(executorService)
                .httpRequest(httpRequest)
                .build();
    }
}
