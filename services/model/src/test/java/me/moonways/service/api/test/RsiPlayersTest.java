package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.model.players.PlayersServiceModel;
import me.moonways.model.players.connection.ConnectedEntityPlayer;
import me.moonways.model.players.connection.PlayerConnection;
import me.moonways.model.players.leveling.PlayerLeveling;

import java.rmi.RemoteException;
import java.util.UUID;

public class RsiPlayersTest {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("players", 7003, PlayersServiceModel.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        PlayersServiceModel stub = accessModule.lookupStub();

        try {
            // players connection management.
            final PlayerConnection playerConnection = stub.getPlayerConnection();

            playerConnection.addConnectedPlayer(
                    new ConnectedEntityPlayer(UUID.randomUUID(), "itzstonlex", null, null)
            );

            ConnectedEntityPlayer connectedPlayer = playerConnection.getConnectedPlayer("itzstonlex");
            System.out.println(connectedPlayer.getUniqueId());

            // players leveling management.
            final PlayerLeveling playerLeveling = stub.getPlayerLeveling();

            int secondLevelExp = playerLeveling.calculateTotalExperience(2);
            System.out.println(secondLevelExp);

            System.out.println(playerLeveling.calculateLevel(secondLevelExp));
            System.out.println(playerLeveling.calculateLevel(secondLevelExp + 1000));
            System.out.println(playerLeveling.calculateExperienceToNextLevel(2));
            System.out.println(playerLeveling.calculateExperiencePercentToNextLevel(secondLevelExp + 5000));
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
