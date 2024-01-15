package me.moonways.bridgenet.mtp;

import io.netty.channel.Channel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@Log4j2
@RequiredArgsConstructor
public class MTPChannel {

    // TODO - 20.07.2023 - Добавить аттрибуты из channel

    @Getter
    private final boolean isClient;

    private final Channel channel;

    @Inject
    private MTPDriver driver;

    public InetSocketAddress address() {
        return ((InetSocketAddress) channel.remoteAddress());
    }

    @Synchronized
    public void sendMessage(@NotNull Object message) {
        ExportedMessage exported = driver.export(message);

        log.info("§9[{}]: §r{}", String.format(getMessageSendLogPrefix(), channel.id()), message);
        channel.writeAndFlush(exported);
    }

    @Synchronized
    public void close() {
        channel.closeFuture();
    }

    private String getMessageSendLogPrefix() {
        return isClient ? "Client[ID=%s] -> Server" : "Server -> Client[ID=%s]";
    }
}
