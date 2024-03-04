package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.parties.PartiesServiceModel;
import me.moonways.bridgenet.model.parties.Party;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class PartiesServiceEndpointTest {

    @Inject
    private PartiesServiceModel serviceModel;

    @Test
    @Order(0)
    public void test_partyCreate() throws RemoteException {
        Party party = serviceModel.createParty("GitCoder");

        assertEquals(party.getOwner().getName(), "GitCoder");
        assertEquals(party.getTotalMembersCount(), 0);
    }
}
