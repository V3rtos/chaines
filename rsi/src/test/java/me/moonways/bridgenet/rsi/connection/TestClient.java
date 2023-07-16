package me.moonways.bridgenet.rsi.connection;

import me.moonways.bridgenet.rsi.TestCalculatorService;
import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;

import java.rmi.RemoteException;

public class TestClient {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("calculator", 225, TestCalculatorService.class);

        AccessModule accessModule = new AccessModule();
        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));

        TestCalculatorService calculator = accessModule.lookupStub();

        try {
            System.out.println(calculator.sum(5, 2));
        }
        catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
