package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.model.friends.FriendsList;
import me.moonways.bridgenet.model.friends.FriendsServiceModel;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class FriendsServiceConnectTest {

    private AccessRemoteModule subj;

    @Before
    public void setUp() {
        ServiceInfo serviceInfo = new ServiceInfo("friends", 7005, FriendsServiceModel.class);

        subj = new AccessRemoteModule();
        subj.init(serviceInfo, new AccessConfig("127.0.0.1"));
    }

    @Test
    public void test_success() {
        FriendsServiceModel stub = subj.lookupStub();

        try {
            UUID friendID = UUID.randomUUID();
            UUID playerID = UUID.randomUUID();

            FriendsList friendsList = stub.findFriends(playerID);
            friendsList.addFriend(friendID);

            assertEquals(1, friendsList.getFriendsIDs().size());
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
