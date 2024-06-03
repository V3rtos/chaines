package me.moonways.bridgenet.mtp.inbound;

import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.mtp.BridgenetNetworkController;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.config.NetworkJsonConfiguration;
import me.moonways.bridgenet.mtp.message.codec.NetworkMessageDecoder;
import me.moonways.bridgenet.mtp.message.codec.NetworkMessageEncoder;
import me.moonways.bridgenet.mtp.message.encryption.MessageEncryption;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Log4j2
@Autobind
public class InboundChannelOptionsHandler extends ChannelInitializer<Channel> {

    private static final int MAX_CONTENT_LENGTH = 1048576;

    @Setter
    private ChannelDirection channelDirection;

    private final List<ChannelInboundHandler> childrenHandlers = new ArrayList<>();
    private Consumer<Channel> initChannelConsumer;

    @Inject
    private BridgenetNetworkController networkController;
    @Inject
    private NetworkJsonConfiguration configuration;
    @Inject
    private BeansService beansService;

    private void initHandlers(@NotNull ChannelPipeline pipeline) {
        InboundChannelMessageHandler handler = new InboundChannelMessageHandler(channelDirection, childrenHandlers);
        addToPipeline(pipeline, handler);
    }

    private void initCodec(@NotNull ChannelPipeline pipeline) {
        addToPipeline(pipeline, new NetworkMessageDecoder(networkController.getNetworkMessagesService(), configuration));
        addToPipeline(pipeline, new InboundChannelMessageAggregator(MAX_CONTENT_LENGTH));
        addToPipeline(pipeline, new NetworkMessageEncoder(configuration));
    }

    public void addChannelHandler(@NotNull ChannelInboundHandler channelHandler) {
        beansService.inject(channelHandler);
        childrenHandlers.add(channelHandler);
    }

    private void initOptions(@NotNull ChannelConfig config) {
        config.setRecvByteBufAllocator(new FixedRecvByteBufAllocator(MAX_CONTENT_LENGTH));
        config.setAllocator(PooledByteBufAllocator.DEFAULT);

        config.setOption(ChannelOption.TCP_NODELAY, true);
        config.setOption(ChannelOption.SO_KEEPALIVE, true);
        config.setOption(ChannelOption.SO_REUSEADDR, true);
        config.setOption(ChannelOption.SO_RCVBUF, MAX_CONTENT_LENGTH);
        config.setOption(ChannelOption.SO_SNDBUF, MAX_CONTENT_LENGTH);
    }

    private void addToPipeline(ChannelPipeline pipeline, ChannelHandler channelHandler) {
        beansService.inject(channelHandler);
        pipeline.addLast(channelHandler);
    }

    @Override
    protected void initChannel(Channel channel) {
        log.info("Running channel {} initialization", channel);

        MessageEncryption encryption = configuration.getEncryption();
        encryption.generateKeys();

        initOptions(channel.config());
        initCodec(channel.pipeline());
        initHandlers(channel.pipeline());

        if (initChannelConsumer != null) {
            initChannelConsumer.accept(channel);
        }
    }

    @NotNull
    public InboundChannelOptionsHandler thenComplete(@NotNull Consumer<Channel> initChannelConsumer) {
        if (this.initChannelConsumer == null)
            this.initChannelConsumer = initChannelConsumer;
        else
            this.initChannelConsumer = this.initChannelConsumer.andThen(initChannelConsumer);
        return this;
    }
}
