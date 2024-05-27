package me.moonways.bridgenet.rsap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.*;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.rsap.api.*;

import java.util.Arrays;

@Log4j2
@Autobind
public final class BridgenetRsapService {

    @Getter
    private ServiceInstanceManager instanceManager;

    @Inject
    private BeansService beansService;

    @Property("rsap.host")
    private WrappedProperty protocolHostProperty;
    @Property("rsap.port")
    private WrappedProperty protocolPortProperty;

    private ServiceInterfaceContext<BridgenetService> createBridgenetServicesContext() {
        int rsapFirstPort = protocolPortProperty.getAsInt().orElse(-1);
        String rsapHostIPv4 = protocolHostProperty.getAsString().orElse(null);

        return ServiceInterfaceContext.forStubs(
                StubRules.<BridgenetService>builder()
                        .hostIPv4(rsapHostIPv4)
                        .indexablePort(rsapFirstPort)
                        .accessFlags(Arrays.asList(
                                AccessingFlag.KEEP_ALIVE,
                                AccessingFlag.SINGLETON_PORT))
                        .rootServiceType(BridgenetService.class)
                        .build());
    }

    @PostConstruct
    private void init() {
        instanceManager = new ServiceInstanceManager(createBridgenetServicesContext());
        beansService.inject(instanceManager);
    }
}
