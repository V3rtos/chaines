package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.connector.BridgenetConnector;
import me.moonways.bridgenet.connector.BridgenetServerSync;
import me.moonways.bridgenet.connector.ConnectedDeviceInfo;
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

    private static final ConnectedDeviceInfo DEVICE_INFO = ConnectedDeviceInfo.builder()
            .name("BungeeCord-1")
            .host("127.0.0.1")
            .port(25565)
            .build();

    private static class TestConnector extends BridgenetConnector {

        @Override
        protected ConnectedDeviceInfo createDeviceInfo() {
            return DEVICE_INFO;
        }

        @Override
        public void onConnected(MTPMessageSender channel) {
            System.out.println("CONNECTED !");
            System.out.println(channel.getClass());
        }
    }

    private static final TestConnector TEST_CONNECTOR = new TestConnector();

    @Before
    public void setUp() {
        TEST_CONNECTOR.doConnectBasically();
    }

    @Test
    public void test_handshakeSuccess() {
        assertNotNull(TEST_CONNECTOR.getServerUuid());
    }

    @Test
    public void test_handshakeFailed() {
        BridgenetServerSync bridgenet = TEST_CONNECTOR.getBridgenetServerSync();

        Handshake.Result result = bridgenet.exchangeHandshake(
                DEVICE_INFO.getName(),
                DEVICE_INFO.getHost(),
                DEVICE_INFO.getPort());

        // server has already registered
        assertTrue(result instanceof Handshake.Failure);
    }
}
