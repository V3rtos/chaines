package me.moonways.bridgenet.rest.client;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.rest.api.HttpHost;
import me.moonways.rest.api.exchange.entity.ExchangeableEntity;
import me.moonways.rest.api.exchange.message.RestMessage;
import me.moonways.rest.api.exchange.message.RestMessageBuilder;
import me.moonways.rest.api.exchange.message.RestMessageType;
import me.moonways.rest.api.exchange.response.RestResponse;
import org.jetbrains.annotations.NotNull;

import java.io.Closeable;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class WrappedHttpClient implements Closeable {

    private static final ExecutorService ASYNC_THREADS_POOL = Threads.newCachedThreadPool();

    public static WrappedHttpClient create(@NotNull HttpHost host) {
        return new WrappedHttpClient(host, new HttpChannel(host));
    }

    private final Map<String, WrappedHttpClient> usedContextsMap
            = Collections.synchronizedMap(new WeakHashMap<>());

    @Getter
    protected final HttpHost host;
    protected final HttpChannel channel;

    public final WrappedHttpClient openContext(String context) {
        String hostname = host.getHost().concat(context);

        if (usedContextsMap.containsKey(hostname)) {
            return usedContextsMap.get(hostname);
        }

        HttpHost httpHost = HttpHost.createNative(hostname, host.getPort());
        WrappedHttpClient httpClient = WrappedHttpClient.create(httpHost);

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

        // apply message params.
        channel.setHeaders(message.getHeaders());
        channel.setType(message.getType());

        // sending rest entity body
        ExchangeableEntity entity = message.getEntity();

        if (entity != null) {
            channel.writeEntity(entity);
            channel.sendRequest();
        }

        return channel.decodeActualityResponse();
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
        Collection<WrappedHttpClient> values = usedContextsMap.values();
        values.forEach(WrappedHttpClient::close);

        return values.size();
    }

    @Override
    public final void close() {
        channel.close(channel.getActiveConnection());
        usedContextsMap.clear();
    }
}
