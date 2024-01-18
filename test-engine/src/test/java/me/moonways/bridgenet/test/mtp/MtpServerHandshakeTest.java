package me.moonways.bridgenet.test.mtp;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFactory;
import io.netty.channel.EventLoopGroup;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.*;
import me.moonways.bridgenet.mtp.message.DefaultMessage;
import me.moonways.bridgenet.mtp.pipeline.NettyPipelineInitializer;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class MtpServerHandshakeTest {

    @Inject
    private DependencyInjection injector;

    @Inject
    private MTPDriver driver;

    private MTPConnectionFactory connectionFactory;
    private MTPChannel channel;

    private void connect() {
        ChannelFactory<? extends Channel> clientChannelFactory = NettyFactory.createClientChannelFactory();

        NettyPipelineInitializer channelInitializer = NettyPipelineInitializer.create(driver, connectionFactory.getConfiguration());
        EventLoopGroup parentWorker = NettyFactory.createEventLoopGroup(2);

        MTPClient client = MTPConnectionFactory.newClientBuilder(connectionFactory)
                .setGroup(parentWorker)
                .setChannelFactory(clientChannelFactory)
                .setChannelInitializer(channelInitializer)
                .build();

        channel = client.connectSync();
        injector.injectFields(channel);
    }

    @Before
    public void bindProtocolThings() {
        connectionFactory = MTPConnectionFactory.createConnectionFactory(injector);
        driver.register(DefaultMessage.class);

        connect();
    }

    private Handshake.Result sendHandshakeMessage() {
        Handshake message = newHandshakeMessage("Test-1");
        CompletableFuture<Handshake.Result> future = channel.sendMessageWithResponse(Handshake.Result.class, message);
        try {
            return future.join();
        } catch (CompletionException exception) {
            return new Handshake.Failure(UUID.randomUUID());
        }
    }

    private Handshake newHandshakeMessage(@SuppressWarnings("SameParameterValue") String name) {
        Properties properties = new Properties();
        properties.setProperty("server.name", name);
        properties.setProperty("server.address.host", "127.0.0.1");
        properties.setProperty("server.address.port", "1298");
        properties.setProperty("server.flag.default", "true");
        return new Handshake(name, Handshake.Type.SERVER, properties);
    }

    @Test
    public void test_handshakeSuccess() {
        Handshake.Result result = sendHandshakeMessage();
        assertTrue(result instanceof Handshake.Success);
    }

    @Test
    public void test_handshakeFailure() {
        Handshake.Result result = sendHandshakeMessage(); // repeat for failure, because uuid has already registered
        assertTrue(result instanceof Handshake.Failure);
    }
}
