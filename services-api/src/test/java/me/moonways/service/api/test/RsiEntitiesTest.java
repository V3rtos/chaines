package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.service.api.entities.BridgenetEntitiesService;
import me.moonways.service.api.entities.player.BridgenetPlayers;
import me.moonways.service.api.entities.player.ConnectedEntityPlayer;

import java.rmi.RemoteException;
import java.util.UUID;

public class RsiEntitiesTest {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("entities", 7003, BridgenetEntitiesService.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        BridgenetEntitiesService stub = accessModule.lookupStub();

        try {
            BridgenetPlayers players = stub.getPlayers();
            players.addConnectedPlayer(
                    new ConnectedEntityPlayer(UUID.randomUUID(), "itzstonlex", null, null)
            );

            ConnectedEntityPlayer entityPlayer = players.getConnectedPlayer("itzstonlex");

            System.out.println(entityPlayer.getUniqueId());
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
