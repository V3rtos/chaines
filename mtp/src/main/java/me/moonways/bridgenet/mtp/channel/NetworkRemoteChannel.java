package me.moonways.bridgenet.mtp.channel;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
    public static final int defaultCallbackTimeout = 1000;

    public static final String pullingStateProperty = "pulling_state";

    private static final AttributeKey<ChannelDirection> directionAttribute = AttributeKey.valueOf("direction_attribute");
    public static final Function<ChannelDirection, String> handleMessageLogFunc = (direction) -> direction == ChannelDirection.TO_SERVER ? "Client[%s] -> Server" : "Server -> Client[%s]";

    @Getter
    private final ChannelDirection direction;
    @Getter
    private final Channel handle;

    @Inject
    private ResponsibleMessageService responsibleService;
    @Inject
    private NetworkMessagesService networkMessagesService;
    @Inject
    private BridgenetNetworkController networkController;

    @PostConstruct
    public void initAttributes() {
        Attribute<ChannelDirection> attribute = handle.attr(directionAttribute);
        attribute.set(direction);
    }

    @Override
    public InetSocketAddress address() {
        return ((InetSocketAddress) handle.remoteAddress());
    }

    @Override
    public synchronized void send(@NotNull Object message) {
        send(networkMessagesService.export(message));
    }

    @Override
    public synchronized void send(@NotNull ExportedMessage exportedMessage) {
        Object message = exportedMessage.getMessage();

        log.debug("§9[{}]: §r{}", String.format(handleMessageLogFunc.apply(direction), handle.remoteAddress()), message);
        handle.writeAndFlush(exportedMessage);
    }

    @Override
    public synchronized void pull(@NotNull Object message) {
        pull(networkMessagesService.export(message));
    }

    @Override
    public synchronized void pull(@NotNull ExportedMessage message) {
        pull(new InboundMessageContext<>(message.getCallbackID(), message.getMessage(), this, System.currentTimeMillis()));
    }

    @Override
    public synchronized void pull(@NotNull InboundMessageContext<?> context) {
        setProperty(pullingStateProperty, true);

        log.debug("§9[PULL]: §r{}", context.getMessage());
        networkController.pull(context);

        setProperty(pullingStateProperty, false);
    }

    @Override
    public synchronized void close() {
        handle.flush();
        handle.close();
    }

    @Override
    public synchronized <R> CompletableFuture<R> sendAwait(int timeout, @NotNull Class<R> responseType, @NotNull Object message) {
        ExportedMessage exportedMessage = networkMessagesService.export(message);
        exportedMessage.marksResponsible(responsibleService);

        send(exportedMessage);

        CompletableFuture<R> future = new CompletableFuture<>();
        responsibleService.await(timeout, exportedMessage.getCallbackID(), future, responseType);

        return future;
    }

    @Override
    public synchronized <R> CompletableFuture<R> sendAwait(@NotNull Class<R> responseType, @NotNull Object message) {
        return sendAwait(defaultCallbackTimeout, responseType, message);
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
