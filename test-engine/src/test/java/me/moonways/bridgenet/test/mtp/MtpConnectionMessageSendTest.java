package me.moonways.bridgenet.test.mtp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.message.DefaultMessage;
import me.moonways.bridgenet.mtp.pipeline.NettyPipeline;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(BridgenetJUnitTestRunner.class)
public class MtpConnectionMessageSendTest {

    @Inject
    private DependencyInjection injector;

    @Inject
    private MTPDriver driver;

    private MTPConnectionFactory connectionFactory;
    private MTPChannel channel;

    @Before
    public void bindMtpThings() {
        connectionFactory = MTPConnectionFactory.createConnectionFactory(dependencyInjection);
        driver.register(DefaultMessage.class);
    }

    @Test
    public void test_successConnect() {
        ChannelFactory<? extends Channel> clientChannelFactory = NettyFactory.createClientChannelFactory();

        NettyPipeline channelInitializer = NettyPipeline.create(driver, connectionFactory.getConfiguration());
        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(2);

        MTPClient client = MTPConnectionFactory.newClientBuilder(connectionFactory)
                .setGroup(parentWorker)
                .setChannelFactory(clientChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        channel = client.connectSync();
        injector.injectFields(channel);
    }

    @Test
    public void test_successMessageSent() {
        DefaultMessage message = DefaultMessage.empty();
        message.setProperty("value", "itzstonlex govnokoder!!!");

        channel.sendMessage(message);
    }
}
