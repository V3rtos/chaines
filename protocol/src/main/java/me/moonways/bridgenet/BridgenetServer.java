package me.moonways.bridgenet;

import io.netty.bootstrap.ServerBootstrap;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Getter
public class BridgenetServer implements BootstrapWorker {

    private final Set<BridgenetChannelExecutor> executorSet = new HashSet<>();

    private final ServerBootstrap serverBootstrap;

    @SneakyThrows
    @Override
    public void bindSync() {
        serverBootstrap.bind().sync();
    }

    @Override
    public void bind() {
        serverBootstrap.bind();
    }

    @Override
    public void shutdownGracefully() {
    }

    public Set<BridgenetChannelExecutor> getExecutors() {
        return executorSet;
    }
}
