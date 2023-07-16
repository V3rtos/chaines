package me.moonways.service.api.test;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.service.api.parties.BridgenetPartiesService;
import me.moonways.service.api.parties.party.Party;

import java.rmi.RemoteException;

public class RsiPartiesTest {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("parties", 7008, BridgenetPartiesService.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        BridgenetPartiesService stub = accessModule.lookupStub();

        try {
            Party party = stub.createParty("GitCoder");

            System.out.println(party.getOwner().getName());
        }
        catch (RemoteException exception) {
            exception.printStackTrace();
        }
    }
}
