package me.moonways.bridgenet.rsi.dynamic;

import lombok.SneakyThrows;
import me.moonways.bridgenet.rsi.module.access.AccessConfig;
import me.moonways.bridgenet.rsi.module.access.AccessModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;

public class ServerApp {

    @SneakyThrows
    public static void main(String[] args) {
        ServiceInfo serviceInfo = new ServiceInfo("dynamic", 1010, DynamicService.class);
        AccessModule accessModule = new AccessModule();

        accessModule.init(serviceInfo, new AccessConfig("127.0.0.1"));
        accessModule.setEndpointClass(DynamicServiceImpl.class);

        accessModule.exportStub(serviceInfo);

        Thread.sleep(10000);
    }
}
