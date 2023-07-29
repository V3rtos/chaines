package me.moonways.bridgenet.rest.api.http.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.api.HttpHost;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessage;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageBuilder;
import me.moonways.bridgenet.rest.api.exchange.message.RestMessageType;
import me.moonways.bridgenet.rest.api.exchange.response.RestResponse;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpClient implements Closeable {

    private static final ExecutorService ASYNC_THREADS_POOL = Executors.newCachedThreadPool();

    public static HttpClient create(@NotNull HttpHost host) {
        return new HttpClient(host, new HttpChannel(host));
    }

    private final Map<String, HttpClient> usedContextsMap
            = Collections.synchronizedMap(new WeakHashMap<>());

    @Getter
    protected final HttpHost host;
    protected final HttpChannel channel;

    public final HttpClient openContext(String context) {
        String hostname = host.getHost().concat(context);

        if (usedContextsMap.containsKey(hostname)) {
            return usedContextsMap.get(hostname);
        }

        HttpHost httpHost = HttpHost.createNative(hostname, host.getPort());
        HttpClient httpClient = HttpClient.create(httpHost);

        usedContextsMap.put(hostname, httpClient);
        return httpClient;
    }

    @SuppressWarnings("resource")
    public final RestResponse executeSync(RestMessage message) {
        HttpChannel channel = this.channel;

        // matches uri context name
        String context = message.getContext();
        if (context != null && context.startsWith("/") && context.length() > 1) {
            channel = openContext(context).channel;
        }

        channel.openConnection();

        // apply message headers.
        channel.setHeaders(channel.getActiveConnection(), message.getHeaders());

        // sending rest entity body
        ExchangeableEntity entity = message.getEntity();

        if (entity != null) {
            channel.sendAndFlush(channel.getActiveConnection(), entity);
        }

        return channel.decodeActualityResponse(channel.getActiveConnection());
    }


    public final RestResponse executeSync(RestMessageType type) {
        return executeSync(RestMessageBuilder.create()
                .setType(type)
                .build());
    }

    public final RestResponse executeSync(RestMessageType type, ExchangeableEntity entity) {
        return executeSync(RestMessageBuilder.create()
                .setType(type)
                .setEntity(entity)
                .build());
    }

    public final CompletableFuture<RestResponse> execute(RestMessage message) {
        return CompletableFuture.supplyAsync(() -> executeSync(message), ASYNC_THREADS_POOL);
    }

    public final CompletableFuture<RestResponse> execute(RestMessageType type) {
        return CompletableFuture.supplyAsync(() -> executeSync(type), ASYNC_THREADS_POOL);
    }

    public final CompletableFuture<RestResponse> execute(RestMessageType type, ExchangeableEntity entity) {
        return CompletableFuture.supplyAsync(() -> executeSync(type, entity), ASYNC_THREADS_POOL);
    }

    public final int cleanup() {
        Collection<HttpClient> values = usedContextsMap.values();
        values.forEach(HttpClient::close);

        return values.size();
    }

    @Override
    public final void close() {
        channel.close(channel.getActiveConnection());
        usedContextsMap.clear();
    }
}
