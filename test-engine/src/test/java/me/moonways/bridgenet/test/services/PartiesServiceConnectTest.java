package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.model.parties.PartiesServiceModel;
import me.moonways.bridgenet.model.parties.Party;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class PartiesServiceConnectTest {

    private AccessRemoteModule subj;

    @Before
    public void setUp() {
        ServiceInfo serviceInfo = new ServiceInfo("parties", 7008, PartiesServiceModel.class);

        subj = new AccessRemoteModule();
        subj.init(serviceInfo, new AccessConfig("127.0.0.1"));
    }

    @Test
    public void test_success() {
        PartiesServiceModel serviceModel = subj.lookupStub();

        try {
            Party party = serviceModel.createParty("GitCoder");

            assertEquals(party.getOwner().getName(), "GitCoder");
            assertEquals(party.getTotalMembersCount(), 0);
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
