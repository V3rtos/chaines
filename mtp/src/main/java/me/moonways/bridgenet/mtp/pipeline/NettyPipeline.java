package me.moonways.bridgenet.mtp.pipeline;

import io.netty.channel.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.mtp.MTPDriver;
import me.moonways.bridgenet.mtp.config.MTPConfiguration;
import me.moonways.bridgenet.mtp.message.codec.MessageDecoder;
import me.moonways.bridgenet.mtp.message.codec.MessageEncoder;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

@Log4j2
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NettyPipeline extends ChannelInitializer<Channel> {

    private static final String ADDITIONAL_CHANNEL_INITIALIZER_ID = "additional_channel_initializer_%d";

    public static NettyPipeline create(MTPDriver driver, MTPConfiguration configuration) {
        return new NettyPipeline(driver, configuration);
    }

    private final MTPDriver driver;
    private final MTPConfiguration configuration;

    private Channel channel;
    private Consumer<Channel> initChannelConsumer;

    private final Set<ChannelInitializer<? extends Channel>> additionalChannelInitializers = new HashSet<>();

    private void initHandlers(@NotNull ChannelPipeline pipeline) {
        pipeline.addLast(new MTPChannelHandler(driver));
    }

    private void initCodec(@NotNull ChannelPipeline pipeline) {
        pipeline.addLast(new MessageDecoder(driver.getMessages(), configuration));
        pipeline.addLast(new MessageEncoder(configuration));
    }

    public void addChannelHandler(@NotNull ChannelHandler channelHandler) {
        channel.pipeline().addLast(channelHandler);
    }

    private void initOptions(@NotNull ChannelConfig config) {
        // todo
    }

    private void initAdditionalInitializers(@NotNull Channel channel) {
        ChannelPipeline pipeline = channel.pipeline();

        int initializerIndex = 0;
        for (ChannelInitializer<? extends Channel> channelInitializer : additionalChannelInitializers) {
            initializerIndex++;

            String identifier = String.format(ADDITIONAL_CHANNEL_INITIALIZER_ID, initializerIndex);
            pipeline.addFirst(identifier, channelInitializer);
        }
    }

    @Override
    protected void initChannel(Channel channel) {
        log.info("Running channel {} initialization", channel);
        this.channel = channel;

        configuration.getEncryption().generateKeys();

        initCodec(channel.pipeline());
        initHandlers(channel.pipeline());
        initOptions(channel.config());

        if (initChannelConsumer != null)
            initChannelConsumer.accept(channel);

        initAdditionalInitializers(channel);
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    @NotNull
    public NettyPipeline thenComplete(@NotNull Consumer<Channel> initChannelConsumer) {
        if (this.initChannelConsumer == null)
            this.initChannelConsumer = initChannelConsumer;
        else
            this.initChannelConsumer.andThen(initChannelConsumer);
        return this;
    }

    @NotNull
    public NettyPipeline addNext(@NotNull ChannelInitializer<? extends Channel> additional) {
        additionalChannelInitializers.add(additional);
        return this;
    }
}
