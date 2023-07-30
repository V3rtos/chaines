package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.model.parties.PartiesServiceModel;
import me.moonways.model.parties.Party;

import java.rmi.RemoteException;

public class RsiPartiesTest {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("parties", 7008, PartiesServiceModel.class);

        AccessRemoteModule accessModule = new AccessRemoteModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        PartiesServiceModel serviceModel = accessModule.lookupStub();

        try {
            Party party = serviceModel.createParty("GitCoder");
            System.out.println(party.getOwner().getName());
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
