package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.connector.description.UserDescription;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.players.OfflinePlayer;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.players.service.PlayerStore;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.players.service.PlayerLeveling;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Properties;

import static org.junit.Assert.*;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class PlayersServiceEndpointTest {

    @Inject
    private PlayersServiceModel serviceModel;
    @Inject
    private BridgenetNetworkChannel channel;

    @BeforeAll
    public void setUp() {
        channel.pull(new Handshake(Handshake.Type.PLAYER,
                prepareUserHandshakeProperties(
                        UserDescription.builder()
                                .proxyId(TestConst.Player.PROXY_ID)
                                .uniqueId(TestConst.Player.ID)
                                .name(TestConst.Player.NICKNAME)
                                .build())));
    }

    @Test
    @TestOrdered(1)
    public void test_playersLevelingMath() throws RemoteException {
        PlayerLeveling playerLeveling = serviceModel.leveling();

        int level2_experience = playerLeveling.totalExperience(2);

        assertEquals(10000, level2_experience);
        assertEquals(2, playerLeveling.toLevel(level2_experience));
        assertEquals(2, playerLeveling.toLevel(level2_experience + 1000));
        assertEquals(12500, playerLeveling.experienceToNextLevel(2));
        assertEquals(0, playerLeveling.experiencePercentToNextLevel(level2_experience + 5000));
    }

    @Test
    @TestOrdered(2)
    public void test_onlinePlayer() throws RemoteException {
        PlayerStore store = serviceModel.store();
        Optional<Player> player = store.get(TestConst.Player.NICKNAME);

        assertFalse(player.isPresent());
    }

    @Test
    @TestOrdered(3)
    public void test_offlinePlayer() throws RemoteException {
        PlayerStore store = serviceModel.store();
        OfflinePlayer offlinePlayer = store.getOffline(TestConst.Player.NICKNAME);

        assertFalse(offlinePlayer.isOnline());

        assertTrue(offlinePlayer.getGroup().isDefault());
        assertTrue(offlinePlayer.getPermissions().isEmpty());

        assertEquals(offlinePlayer.getId(), TestConst.Player.ID);

        assertEquals(1, offlinePlayer.getLevel());
        assertEquals(0, offlinePlayer.getTotalExperience());
    }

    private static Properties prepareUserHandshakeProperties(UserDescription description) {
        Properties properties = new Properties();
        properties.setProperty("user.name", description.getName());
        properties.setProperty("user.uuid", description.getUniqueId().toString());
        properties.setProperty("user.proxy", description.getProxyId().toString());
        return properties;
    }
}
