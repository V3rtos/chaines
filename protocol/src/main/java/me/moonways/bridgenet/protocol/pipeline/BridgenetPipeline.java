package me.moonways.bridgenet.protocol.pipeline;

import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.Builder;
import me.moonways.bridgenet.protocol.ProtocolControl;
import me.moonways.bridgenet.protocol.pipeline.codec.MessageDecoder;
import me.moonways.bridgenet.protocol.pipeline.codec.MessageEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Builder(setterPrefix = "set", builderClassName = "Builder", builderMethodName = "newFullBuilder")
public class BridgenetPipeline extends ChannelInitializer<SocketChannel> {

    private static final String ADDITIONAL_CHANNEL_INITIALIZER_ID = "additional_channel_initializer_%d";

    public static Builder newBuilder(@NotNull ProtocolControl protocolControl) {
        return BridgenetPipeline.newFullBuilder().setProtocolControl(protocolControl);
    }

    private final ProtocolControl protocolControl;

    private SocketChannel socketChannel;

    private Consumer<SocketChannel> initChannelConsumer;

    private final Set<ChannelInitializer<? extends SocketChannel>> additionalChannelInitializers = new HashSet<>();

    private void initHandlers(@NotNull ChannelPipeline pipeline) {
        pipeline.addLast(new BridgenetChannelHandler(protocolControl, protocolControl.getTriggerHandler()));
    }

    private void initCodec(@NotNull ChannelPipeline pipeline) {
        pipeline.addLast(new MessageDecoder(protocolControl.getRegistrationService()));
        pipeline.addLast(new MessageEncoder(protocolControl.getRegistrationService()));
    }

    public void addLast(@NotNull ChannelHandler channelHandler) {
        socketChannel.pipeline().addLast(channelHandler);
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
        this.socketChannel = socketChannel;

        initCodec(socketChannel.pipeline());
        initHandlers(socketChannel.pipeline());
        initOptions(socketChannel.config());

        if (initChannelConsumer != null)
            initChannelConsumer.accept(socketChannel);

        initAdditionalInitializers(socketChannel);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @NotNull
    public BridgenetPipeline thenComplete(@NotNull Consumer<SocketChannel> initChannelConsumer) {
        if (this.initChannelConsumer == null)
            this.initChannelConsumer = initChannelConsumer;
        else
            this.initChannelConsumer.andThen(initChannelConsumer);
        return this;
    }

    @NotNull
    public BridgenetPipeline addNext(@NotNull ChannelInitializer<? extends SocketChannel> additional) {
        additionalChannelInitializers.add(additional);
        return this;
    }
}
