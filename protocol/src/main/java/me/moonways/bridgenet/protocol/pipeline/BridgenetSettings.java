package me.moonways.bridgenet.protocol.pipeline;

import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Builder;
import lombok.Setter;
import me.moonways.bridgenet.protocol.Bridgenet;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import me.moonways.bridgenet.protocol.pipeline.handler.BridgenetChannelHandler;
import me.moonways.bridgenet.protocol.message.BridgenetMessageHandler;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Builder(setterPrefix = "set", builderClassName = "Builder", builderMethodName = "newFullBuilder")
public class BridgenetSettings extends ChannelInitializer<SocketChannel> {

    private static final String ADDITIONAL_CHANNEL_INITIALIZER_ID = "additional_channel_initializer_%d";

    public static Builder newBuilder(@NotNull Bridgenet bridgenet) {
        return BridgenetSettings.newFullBuilder().setBridgenet(bridgenet);
    }

    private final Bridgenet bridgenet;

    private Consumer<SocketChannel> initChannelConsumer;

    @Setter
    private BridgenetMessageHandlerFactory bridgenetMessageHandlerFactory;

    private final Set<ChannelInitializer<? extends SocketChannel>> additionalChannelInitializers = new HashSet<>();

    private void initHandlers(@NotNull ChannelPipeline pipeline) {
        MessageContainer messageContainer = bridgenet.getMessageContainer();
        BridgenetMessageHandler messageHandler = bridgenetMessageHandlerFactory.create(messageContainer);

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
    public BridgenetSettings thenComplete(@NotNull Consumer<SocketChannel> initChannelConsumer) {
        if (this.initChannelConsumer == null)
            this.initChannelConsumer = initChannelConsumer;
        else
            this.initChannelConsumer.andThen(initChannelConsumer);
        return this;
    }

    @NotNull
    public BridgenetSettings addNext(@NotNull ChannelInitializer<? extends SocketChannel> additional) {
        additionalChannelInitializers.add(additional);
        return this;
    }
}
