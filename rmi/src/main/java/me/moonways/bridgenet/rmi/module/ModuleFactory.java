package me.moonways.bridgenet.rmi.module;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rmi.service.ServiceInfo;

import java.util.function.Function;

@RequiredArgsConstructor
public final class ModuleFactory {

    @Getter
    private final ModuleID id;
    private final Function<ServiceInfo, RemoteModule<?>> factoryFunc;

    public RemoteModule<?> create(ServiceInfo serviceInfo) {
        return factoryFunc.apply(serviceInfo);
    }
}
