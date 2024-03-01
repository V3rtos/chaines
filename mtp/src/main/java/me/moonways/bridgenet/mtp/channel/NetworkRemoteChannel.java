package me.moonways.bridgenet.mtp.channel;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.NetworkMessagesService;
import me.moonways.bridgenet.mtp.message.response.ResponsibleMessageService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Log4j2
@RequiredArgsConstructor
public class NetworkRemoteChannel implements BridgenetNetworkChannel {

    private static final long serialVersionUID = -4718332193161413564L;

    public static final AttributeKey<ChannelDirection> DIRECTION_ATTRIBUTE = AttributeKey.valueOf("direction_attribute");
    public static final Function<ChannelDirection, String> MESSAGE_HANDLE_LOG_MSG = (direction) -> direction == ChannelDirection.TO_SERVER ? "Client[%s] -> Server" : "Server -> Client[%s]";
    public static final int DEFAULT_RESPONSE_TIMEOUT = 5000;

    @Getter
    private final ChannelDirection direction;

    @Getter
    private final Channel handle;

    private long lastResponseSessionId;

    @Inject
    private ResponsibleMessageService responsibleService;
    @Inject
    private NetworkMessagesService networkMessagesService;
    @Inject
    private BridgenetNetworkController networkController;

    @PostConstruct
    public void initAttributes() {
        Attribute<ChannelDirection> attribute = handle.attr(DIRECTION_ATTRIBUTE);
        attribute.set(direction);
    }

    @Override
    public InetSocketAddress address() {
        return ((InetSocketAddress) handle.remoteAddress());
    }

    @Synchronized
    @Override
    public void send(@NotNull Object message) {
        ExportedMessage exported = networkMessagesService.export(message);

        log.info("§9[{}]: §r{}", String.format(MESSAGE_HANDLE_LOG_MSG.apply(direction), handle.remoteAddress()), message);
        handle.writeAndFlush(exported);
    }

    @Override
    public void pull(@NotNull Object message) {
        pull(new InboundMessageContext<>(message, this, System.currentTimeMillis()));
    }

    @Override
    public void pull(@NotNull InboundMessageContext<?> context) {
        networkController.handle(context);
    }

    @Synchronized
    @Override
    public void close() {
        handle.closeFuture();
    }

    @Synchronized
    @Override
    public <R> CompletableFuture<R> sendAwait(int timeout, @NotNull Class<R> responseType, @NotNull Object message) {
        send(message);

        CompletableFuture<R> future = new CompletableFuture<>();
        responsibleService.await(timeout, future, responseType);

        return future;
    }

    @Synchronized
    @Override
    public <R> CompletableFuture<R> sendAwait(@NotNull Class<R> responseType, @NotNull Object message) {
        return sendAwait(DEFAULT_RESPONSE_TIMEOUT, responseType, message);
    }

    private long createResponseSessionId() {
        if (lastResponseSessionId + 1 == Long.MAX_VALUE) {
            return lastResponseSessionId = 0;
        }
        return lastResponseSessionId++;
    }

    @Override
    public <T> Optional<T> getProperty(@NotNull String key) {
        Attribute<T> attribute = handle.attr(AttributeKey.valueOf(key));
        return Optional.ofNullable(attribute.get());
    }

    @Override
    public void setProperty(@NotNull String key, @Nullable Object value) {
        Attribute<Object> attribute = handle.attr(AttributeKey.valueOf(key));
        attribute.set(value);
    }
}
