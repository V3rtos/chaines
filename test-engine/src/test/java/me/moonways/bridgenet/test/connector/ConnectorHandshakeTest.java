package me.moonways.bridgenet.test.connector;

import me.moonways.bridgenet.api.command.api.process.CommandService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.test.connector.subj.TestConnector;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(BridgenetJUnitTestRunner.class)
public class ConnectorHandshakeTest {

    private final TestConnector subj = new TestConnector();

    @Inject
    private CommandService commandService;

    @Test
    @Order(0)
    public void test_handshakeSuccess() {
        subj.start();
        assertNotNull(subj.getCurrentDeviceId());

        command();
    }

    @Test
    @Order(1)
    public void test_handshakeFailed() {
        Handshake.Result result = subj.retryHandshakeExchanging();
        assertTrue(result instanceof Handshake.Failure);
    }

    private void command() {
        commandService.dispatchConsole("servers list");
        commandService.dispatchConsole("servers get " + TestConnector.DEVICE_DESCRIPTION.getName());
    }
}
