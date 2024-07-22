package me.moonways.bridgenet.test.rest;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rest4j.Bridgenet4jRestClient;
import me.moonways.bridgenet.rest4j.Response;
import me.moonways.bridgenet.rest4j.data.OkPlayerStatus;
import me.moonways.bridgenet.rest4j.data.OkServer;
import me.moonways.bridgenet.rest4j.data.OkServersList;
import me.moonways.bridgenet.rest4j.data.OkTotalOnline;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.component.module.impl.ClientsModule;
import me.moonways.bridgenet.test.engine.component.module.impl.RestModule;
import me.moonways.bridgenet.test.engine.component.step.impl.JoinPlayerStep;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import me.moonways.bridgenet.test.engine.persistance.BeforeSteps;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@Log4j2
@RunWith(ModernTestEngineRunner.class)
@TestModules({RestModule.class, ClientsModule.class})
@BeforeSteps(JoinPlayerStep.class)
public class Bridgenet4jClientTest {

    private Bridgenet4jRestClient bridgenet4jClient;

    @BeforeAll
    public void setUp() {
        bridgenet4jClient = Bridgenet4jRestClient.create(
                TestConst.Rest.BASEURL,
                TestConst.Rest.API_ACCESS_KEY);
    }

    @Test
    public void test_playersGetTotalOnline() {
        Response<OkTotalOnline> totalOnlineResponse = bridgenet4jClient.getTotalOnline()
                .printError(System.out)
                .subscribeOk(log::debug);

        assertNotNull(totalOnlineResponse.getOk());
        assertNull(totalOnlineResponse.getError());
        assertEquals(totalOnlineResponse.getOk().getValue(), 1);
    }

    @Test
    public void test_playersGetPlayerStatus() {
        Response<OkPlayerStatus> playerStatusResponse = bridgenet4jClient.getPlayerStatus(TestConst.Player.NICKNAME)
                .printError(System.out)
                .subscribeOk(log::debug);

        assertNotNull(playerStatusResponse.getOk());
        assertNull(playerStatusResponse.getError());
        assertTrue(playerStatusResponse.getOk().isOnline());
        assertNotNull(playerStatusResponse.getOk().getServer());
        assertEquals(playerStatusResponse.getOk().getName(), TestConst.Player.NICKNAME);
        assertEquals(playerStatusResponse.getOk().getServer().getName(), TestConst.Server.NAME);
    }

    @Test
    public void test_serversList() {
        Response<OkServersList> serversListResponse = bridgenet4jClient.getServersList()
                .printError(System.out)
                .subscribeOk(log::debug);

        assertNotNull(serversListResponse.getOk());
        assertNull(serversListResponse.getError());
        assertEquals(serversListResponse.getOk().getList().size(), 1);
    }

    @Test
    public void test_serversGetServer() {
        Response<OkServer> serverResponse = bridgenet4jClient.getServer(TestConst.Server.NAME)
                .printError(System.out)
                .subscribeOk(log::debug);

        assertNotNull(serverResponse.getOk());
        assertNull(serverResponse.getError());
        assertEquals(serverResponse.getOk().getOnline(), 1);
    }
}
