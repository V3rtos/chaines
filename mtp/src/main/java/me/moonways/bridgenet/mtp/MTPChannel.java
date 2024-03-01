package me.moonways.bridgenet.mtp;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.InputMessageContext;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.response.ResponsibleMessageService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.InetSocketAddress;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Log4j2
@RequiredArgsConstructor
public class MTPChannel implements MTPMessageSender {

    private static final long serialVersionUID = -4718332193161413564L;

    public static final AttributeKey<ProtocolDirection> DIRECTION_ATTRIBUTE = AttributeKey.valueOf("direction_attribute");
    public static final int DEFAULT_RESPONSE_TIMEOUT = 5000;

    @Getter
    private final ProtocolDirection direction;

    @Getter
    private final Channel handle;
    private long lastResponseSessionId;

    @Inject
    private ResponsibleMessageService responseService;
    @Inject
    private MessageRegistry messageRegistry;
    @Inject
    private BeansService beansService;
    @Inject
    private MTPDriver driver;

    @PostConstruct
    public void initAttributes() {
        Attribute<ProtocolDirection> attribute = handle.attr(DIRECTION_ATTRIBUTE);
        attribute.set(direction);
    }

    public InetSocketAddress address() {
        return ((InetSocketAddress) handle.remoteAddress());
    }

    @Synchronized
    @Override
    public void sendMessage(@NotNull Object message) {
        ExportedMessage exported = messageRegistry.export(message);

        log.info("§9[{}]: §r{}", String.format(getMessageSendLogPrefix(), handle.remoteAddress()), message);
        handle.writeAndFlush(exported);
    }

    @Override
    public void sendInsideMessage(@NotNull Object message) {
        driver.handle(new InputMessageContext<>(message, this, System.currentTimeMillis()));
    }

    @Synchronized
    @Override
    public <R> CompletableFuture<R> sendMessageWithResponse(int timeout, @NotNull Class<R> responseType, @NotNull Object message) {
        //sendMessage(new ResponsibleMessage(
        //        createResponseSessionId(), driver.export(message)
        //));
        sendMessage(message);

        CompletableFuture<R> future = new CompletableFuture<>();
        responseService.await(timeout, future, responseType);

        return future;
    }

    @Synchronized
    @Override
    public <R> CompletableFuture<R> sendMessageWithResponse(@NotNull Class<R> responseType, @NotNull Object message) {
        return sendMessageWithResponse(DEFAULT_RESPONSE_TIMEOUT, responseType, message);
    }

    @Synchronized
    @Override
    public void close() {
        handle.closeFuture();
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

    public String getMessageSendLogPrefix() {
        return direction == ProtocolDirection.TO_CLIENT ? "Client[%s] -> Server" : "Server -> Client[%s]";
    }
}
