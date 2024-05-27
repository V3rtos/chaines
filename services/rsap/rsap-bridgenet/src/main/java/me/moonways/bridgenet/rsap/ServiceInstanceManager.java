package me.moonways.bridgenet.rsap;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.rsap.api.ServiceInterfaceContext;
import me.moonways.rsap.api.StubAccess;

@Log4j2
@RequiredArgsConstructor
public final class ServiceInstanceManager {

    private final ServiceInterfaceContext<BridgenetService> interfaceContext;

    @Inject
    private BeansService beansService;

    public void add(Class<? extends BridgenetService> interfaceType, BridgenetService service) {
        if (!interfaceType.isInterface()) {
            throw new BridgenetRsapException(interfaceType + " is not interface");
        }

        StubAccess<BridgenetService> serviceStub = interfaceContext.export(0, service);

        if (!serviceStub.isExported()) {
            log.info("Service `{}` exporting is failed: {}", serviceStub.getName(), serviceStub.getAddress());
            return;
        }

        BridgenetService bridgenetServiceStub = serviceStub.getProxiedService().get();

        beansService.bind(service.getClass(), bridgenetServiceStub);
        log.info("Service `{}` was success exported: {}", serviceStub.getName(), serviceStub.getAddress());
    }

    public <T extends BridgenetService> T get(Class<T> serviceClass) {
        if (!serviceClass.isInterface()) {
            throw new BridgenetRsapException(serviceClass + " is not interface");
        }

        return null;
    }
}
