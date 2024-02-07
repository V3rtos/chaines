package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.mtp.MTPMessageSender;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class BridgenetConnectorTest {

    private static class TestConnector extends BridgenetConnector {

        @Override
        public void onConnected(MTPMessageSender channel) {
            System.out.println("CONNECTED !");
            System.out.println(channel.getClass());
        }
    }

    private static final TestConnector TEST_CONNECTOR = new TestConnector();

    @Inject
    private BeansService beans;

    @Before
    public void setUp() {
        beans.bind(TEST_CONNECTOR);
    }

    @Test
    public void test_handshakeSuccess() {
        Handshake.Result result = TEST_CONNECTOR.sendServerHandshake("BungeeCord-1", "127.0.0.1", 25565);

        assertTrue(result instanceof Handshake.Success);
        assertNotNull(TEST_CONNECTOR.getServerUuid());
    }

    @Test
    public void test_handshakeFailed() {
        Handshake.Result result = TEST_CONNECTOR.sendServerHandshake("BungeeCord-1", "127.0.0.1", 25565);
        assertTrue(result instanceof Handshake.Failure); // server has already registered
    }
}
