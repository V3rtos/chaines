package me.moonways.bridgenet.mtp.channel;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@RequiredArgsConstructor
public final class NetworkReferenceChannel implements BridgenetNetworkChannel {

    private static final long serialVersionUID = 8245149925161062394L;

    private final transient AtomicReference<BridgenetNetworkChannel> channelRef;

    @Override
    public InetSocketAddress address() {
        return channelRef.get().address();
    }

    @Override
    public <T> Optional<T> getProperty(@NotNull String key) {
        return channelRef.get().getProperty(key);
    }

    @Override
    public void setProperty(@NotNull String key, @Nullable Object value) {
        channelRef.get().setProperty(key, value);
    }

    @Override
    public void send(@NotNull Object message) {
        channelRef.get().send(message);
    }

    @Override
    public void pull(@NotNull Object message) {
        channelRef.get().pull(message);
    }

    @Override
    public void pull(@NotNull InboundMessageContext<?> context) {
        channelRef.get().pull(context);
    }

    @Override
    public void close() {
        channelRef.get().close();
    }

    @Override
    public <R> CompletableFuture<R> sendAwait(@NotNull Class<R> responseType, @NotNull Object message) {
        return channelRef.get().sendAwait(responseType, message);
    }

    @Override
    public <R> CompletableFuture<R> sendAwait(int timeout, @NotNull Class<R> responseType, @NotNull Object message) {
        return channelRef.get().sendAwait(timeout, responseType, message);
    }
}