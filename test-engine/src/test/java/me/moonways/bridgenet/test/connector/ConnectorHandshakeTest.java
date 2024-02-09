package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.test.connector.subj.TestConnector;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class ConnectorHandshakeTest {

    private final TestConnector subj = new TestConnector();

    @Before
    public void setUp() {
        subj.doConnectBasically();
    }

    @Test
    public void test_handshakeSuccess() {
        assertNotNull(subj.getServerUuid());
    }

    @Test
    public void test_handshakeFailed() {
        Handshake.Result result = subj.retryHandshakeExchanging();
        assertTrue(result instanceof Handshake.Failure);
    }
}
