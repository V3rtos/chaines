package me.moonways.bridgenet.rsi.dynamic;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;

import java.rmi.RemoteException;

public class ClientApp {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("dynamic", 1010, DynamicService.class);

        AccessRemoteModule accessModule = new AccessRemoteModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        DynamicService service = accessModule.lookupStub();

        try {
            IDynamicEmulator emulator = service.getEmulator();
            emulator.sayHello();
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
