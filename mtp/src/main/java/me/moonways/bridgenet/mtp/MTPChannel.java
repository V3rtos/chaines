package me.moonways.bridgenet.mtp;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.ResponsibleMessage;
import me.moonways.bridgenet.mtp.pipeline.response.DefaultMessageResponseService;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
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
    public void close() {
        channel.closeFuture();
    }

    private long createResponseSessionId() {
        if (lastResponseSessionId + 1 == Long.MAX_VALUE) {
            return lastResponseSessionId = 0;
        }
        return lastResponseSessionId++;
    }

    private String getMessageSendLogPrefix() {
        return isClient ? "Client[ID=%s] -> Server" : "Server -> Client[ID=%s]";
    }
}
