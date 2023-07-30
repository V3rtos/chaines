package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.model.friends.FriendsList;
import me.moonways.model.friends.FriendsServiceModel;

import java.rmi.RemoteException;
import java.util.UUID;

public class RsiFriendsTest {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("friends", 7005, FriendsServiceModel.class);

        AccessRemoteModule accessModule = new AccessRemoteModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        FriendsServiceModel stub = accessModule.lookupStub();

        try {
            FriendsList friendsList = stub.findFriends(UUID.randomUUID());
            System.out.println(friendsList);

            friendsList.addFriend(UUID.randomUUID());
            System.out.println(friendsList.getFriendsUUIDs());
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
