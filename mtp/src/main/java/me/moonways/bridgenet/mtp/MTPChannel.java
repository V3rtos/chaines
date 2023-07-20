package me.moonways.bridgenet.mtp;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

@RequiredArgsConstructor
public class MTPChannel {

    // TODO - 20.07.2023 - Добавить аттрибуты из channel

    private final Channel channel;

    @Inject
    private MTPDriver driver;

    public InetSocketAddress address() {
        return ((InetSocketAddress) channel.remoteAddress());
    }

    @Synchronized
    public void sendMessage(@NotNull Object message) {
        ExportedMessage exported = driver.export(message);
        channel.writeAndFlush(exported);
    }

    @Synchronized
    public void close() {
        channel.closeFuture();
    }
}
