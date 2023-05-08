package me.moonways.bridgenet.pipeline;

import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Builder;
import lombok.Setter;
import me.moonways.bridgenet.Bridgenet;
import me.moonways.bridgenet.BridgenetChannelHandler;
import me.moonways.bridgenet.BridgenetMessageHandler;
import me.moonways.bridgenet.MessageRegistryContainer;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Builder(setterPrefix = "set", builderClassName = "Builder", builderMethodName = "newFullBuilder")
public class BridgenetConfiguration extends ChannelInitializer<SocketChannel> {

    private static final String ADDITIONAL_CHANNEL_INITIALIZER_ID = "additional_channel_initializer_%d";

    public static Builder newBuilder(@NotNull Bridgenet bridgenet) {
        return BridgenetConfiguration.newFullBuilder().setBridgenet(bridgenet);
    }

    private final Bridgenet bridgenet;

    private Consumer<SocketChannel> initChannelConsumer;

    @Setter
    private BridgenetMessageHandlerFactory bridgenetMessageHandlerFactory;

    private final Set<ChannelInitializer<? extends SocketChannel>> additionalChannelInitializers = new HashSet<>();

    private void initHandlers(@NotNull ChannelPipeline pipeline) {
        MessageRegistryContainer messageRegistryContainer = bridgenet.getMessageRegistryContainer();
        BridgenetMessageHandler messageHandler = bridgenetMessageHandlerFactory.create(messageRegistryContainer);

        pipeline.addLast(new BridgenetChannelHandler(messageHandler));
    }

    private void initOptions(@NotNull ChannelConfig config) {
        // todo
    }

    private void initAdditionalInitializers(@NotNull SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();

        int initializerIndex = 0;
        for (ChannelInitializer<? extends SocketChannel> channelInitializer : additionalChannelInitializers) {
            initializerIndex++;

            String identifier = String.format(ADDITIONAL_CHANNEL_INITIALIZER_ID, initializerIndex);
            pipeline.addFirst(identifier, channelInitializer);
        }
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        initHandlers(socketChannel.pipeline());
        initOptions(socketChannel.config());

        if (initChannelConsumer != null)
            initChannelConsumer.accept(socketChannel);

        initAdditionalInitializers(socketChannel);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @NotNull
    public BridgenetConfiguration thenComplete(@NotNull Consumer<SocketChannel> initChannelConsumer) {
        if (this.initChannelConsumer == null)
            this.initChannelConsumer = initChannelConsumer;
        else
            this.initChannelConsumer.andThen(initChannelConsumer);
        return this;
    }

    @NotNull
    public BridgenetConfiguration addNext(@NotNull ChannelInitializer<? extends SocketChannel> additional) {
        additionalChannelInitializers.add(additional);
        return this;
    }
}
