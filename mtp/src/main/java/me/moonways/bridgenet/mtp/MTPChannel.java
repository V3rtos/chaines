package me.moonways.bridgenet.mtp;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class MTPChannel {

    // TODO - 20.07.2023 - Добавить аттрибуты из channel

    private final Channel channel;

    public InetSocketAddress address() {
        return ((InetSocketAddress) channel.remoteAddress());
    }

    @Synchronized
    public void sendMessage(@NotNull MessageWrapper message) {
        channel.writeAndFlush(message);
    }

    @Synchronized
    public void close() {
        channel.closeFuture();
    }
}
