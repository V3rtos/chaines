package me.moonways.bridgenet.mtp;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.pipeline.response.DefaultMessageResponseService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
public class MTPChannel implements MTPMessageSender {

    private static final int DEFAULT_RESPONSE_TIMEOUT = 5000;

    @Getter
    private final boolean isClient;

    private final Channel channel;
    private long lastResponseSessionId;

    @Inject
    private MTPDriver driver;
    @Inject
    private DefaultMessageResponseService responseService;

    public InetSocketAddress address() {
        return ((InetSocketAddress) channel.remoteAddress());
    }

    @Synchronized
    @Override
    public void sendMessage(@NotNull Object message) {
        ExportedMessage exported = driver.export(message);

        log.info("§9[{}]: §r{}", String.format(getMessageSendLogPrefix(), channel.id()), message);
        channel.writeAndFlush(exported);
    }

    @Synchronized
    @Override
    public <R> CompletableFuture<R> sendMessageWithResponse(int timeout, @NotNull Class<R> responseType, @NotNull Object message) {
        //sendMessage(new ResponsibleMessage(
        //        createResponseSessionId(), driver.export(message)
        //));
        CompletableFuture<R> future = new CompletableFuture<>();
        responseService.await(timeout, future, responseType);

        sendMessage(message);

        return future;
    }

    @Synchronized
    @Override
    public <R> CompletableFuture<R> sendMessageWithResponse(@NotNull Class<R> responseType, @NotNull Object message) {
        return sendMessageWithResponse(DEFAULT_RESPONSE_TIMEOUT, responseType, message);
    }

    @Synchronized
    public void close() {
        channel.closeFuture();
    }

    private long createResponseSessionId() {
        if (lastResponseSessionId + 1 == Long.MAX_VALUE) {
            return lastResponseSessionId = 0;
        }
        return lastResponseSessionId++;
    }

    @Override
    public Optional<Object> getProperty(@NotNull String key) {
        Attribute<Object> attribute = channel.attr(AttributeKey.valueOf(key));
        return Optional.ofNullable(attribute.get());
    }

    @Override
    public Optional<String> getPropertyString(@NotNull String key) {
        return getProperty(key).map(Object::toString);
    }

    @Override
    public Optional<Integer> getPropertyInt(@NotNull String key) {
        return getPropertyString(key).map(Integer::parseInt);
    }

    @Override
    public void setProperty(@NotNull String key, @Nullable Object value) {
        Attribute<Object> attribute = channel.attr(AttributeKey.valueOf(key));
        attribute.set(value);
    }

    private String getMessageSendLogPrefix() {
        return isClient ? "Client[ID=%s] -> Server" : "Server -> Client[ID=%s]";
    }
}
