package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.model.friends.FriendsList;
import me.moonways.bridgenet.model.friends.FriendsServiceModel;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class FriendsServiceEndpointTest {

    @Inject
    private FriendsServiceModel friendsServiceModel;

    @Test
    @Order(0)
    public void test_friendAdd() throws RemoteException {
        UUID friendID = UUID.randomUUID();
        UUID playerID = UUID.randomUUID();

        FriendsList friendsList = friendsServiceModel.getFriends(playerID);
        friendsList.addFriend(friendID);

        assertEquals(1, friendsList.getFriendsIDs().size());
    }
}
