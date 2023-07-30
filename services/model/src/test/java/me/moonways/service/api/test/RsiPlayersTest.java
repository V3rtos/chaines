package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.model.players.PlayersServiceModel;
import me.moonways.model.players.ConnectedEntityPlayer;

import java.rmi.RemoteException;
import java.util.UUID;

public class RsiPlayersTest {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("players", 7003, PlayersServiceModel.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        PlayersServiceModel stub = accessModule.lookupStub();

        try {
            stub.addConnectedPlayer(
                    new ConnectedEntityPlayer(UUID.randomUUID(), "itzstonlex", null, null)
            );

            ConnectedEntityPlayer entityPlayer = stub.getConnectedPlayer("itzstonlex");

            System.out.println(entityPlayer.getUniqueId());
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
