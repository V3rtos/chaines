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
import me.moonways.bridgenet.test.engine.util.TestMTPClientConnection;
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
    private TestMTPClientConnection clientConnection;

    @Before
    public void bindProtocolThings() {
        clientConnection.prepareTest();
    }

    private Handshake.Result sendHandshakeMessage() {
        MTPChannel channel = clientConnection.getChannel();

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
