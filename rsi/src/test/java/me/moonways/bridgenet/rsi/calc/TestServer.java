package me.moonways.bridgenet.rsi.calc;

import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;

public class TestServer {

    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("calculator", 225, TestCalculatorService.class);
        AccessRemoteModule accessModule = new AccessRemoteModule();

        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));
        accessModule.setEndpointClass(TestCalculatorServiceObject.class);

        accessModule.exportStub(serviceInfo);
    }
}
