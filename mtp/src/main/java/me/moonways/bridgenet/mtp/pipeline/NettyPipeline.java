package me.moonways.bridgenet.mtp.pipeline;

import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.message.codec.MessageDecoder;
import me.moonways.bridgenet.mtp.message.codec.MessageEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NettyPipeline extends ChannelInitializer<SocketChannel> {

    private static final String ADDITIONAL_CHANNEL_INITIALIZER_ID = "additional_channel_initializer_%d";

    public static NettyPipeline create(MTPDriver driver) {
        return new NettyPipeline(driver);
    }

    private final MTPDriver driver;

    private SocketChannel socketChannel;

    private Consumer<SocketChannel> initChannelConsumer;

    private final Set<ChannelInitializer<? extends SocketChannel>> additionalChannelInitializers = new HashSet<>();

    private void initHandlers(@NotNull ChannelPipeline pipeline) {
        log.info("Initialized channel handlers");

        pipeline.addLast(new NettyChannelHandler(driver));
    }

    private void initCodec(@NotNull ChannelPipeline pipeline) {
        log.info("Initialized messages channel codecs");

        pipeline.addLast(new MessageDecoder(driver.getMessages()));
        pipeline.addLast(new MessageEncoder(driver.getMessages()));
    }

    public void addChannelHandler(@NotNull ChannelHandler channelHandler) {
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
        log.info("Running SocketChannel initialization");

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
    public NettyPipeline thenComplete(@NotNull Consumer<SocketChannel> initChannelConsumer) {
        if (this.initChannelConsumer == null)
            this.initChannelConsumer = initChannelConsumer;
        else
            this.initChannelConsumer.andThen(initChannelConsumer);
        return this;
    }

    @NotNull
    public NettyPipeline addNext(@NotNull ChannelInitializer<? extends SocketChannel> additional) {
        additionalChannelInitializers.add(additional);
        return this;
    }
}
