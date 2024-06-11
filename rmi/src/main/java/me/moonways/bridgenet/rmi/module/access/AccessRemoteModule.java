package me.moonways.bridgenet.rmi.module.access;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.rmi.module.AbstractRemoteModule;
import me.moonways.bridgenet.rmi.module.ModuleConst;
import me.moonways.bridgenet.rmi.module.ModuleID;
import me.moonways.bridgenet.rmi.service.RemoteService;
import me.moonways.bridgenet.rmi.service.RemoteServicesManagement;
import me.moonways.bridgenet.rmi.service.ServiceInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

@Log4j2
public class AccessRemoteModule extends AbstractRemoteModule<AccessConfig> {

    private static final String URI_FORMAT = "rmi://%s:%d/%s";

    private String uri;

    @Setter
    private Class<?> endpointClass;

    @Inject
    private BeansService beansService;
    @Inject
    private RemoteServicesManagement remoteServicesManagement;

    public AccessRemoteModule() {
        super(ModuleID.of(ModuleConst.REMOTE_ACCESS_ID, "accessModule"));
    }

    @Override
    public void init(ServiceInfo serviceInfo, AccessConfig config) {
        String host = config.getRemoteHost();
        int port = serviceInfo.getPort();

        uri = String.format(URI_FORMAT, host, port, serviceInfo.getName());
    }

    public RemoteService exportStub(@NotNull ServiceInfo serviceInfo) {
        String name = serviceInfo.getName();
        Class<? extends RemoteService> subclass = endpointClass.asSubclass(RemoteService.class);

        try {
            Constructor<? extends RemoteService> constructor = subclass.getConstructor();
            RemoteService stub = constructor.newInstance();

            log.info("Exporting endpoint '{}' from §3{} §rto §9{}", name, serviceInfo.getModelClass().getSimpleName(), stub.getClass().getSimpleName());
            beansService.bind(serviceInfo.getModelClass(), stub);

            try {
                LocateRegistry.createRegistry(serviceInfo.getPort());
                Naming.rebind(uri, stub);

                remoteServicesManagement.registerService(serviceInfo, stub);
            } catch (RemoteException exception) {
                log.error("§4Cannot be export an endpoint from {}: §c{}", uri, exception.toString());
            }
            return stub;
        } catch (Exception exception) {
            log.error("§4Cannot be export an endpoint '{}'", name, exception);
            return null;
        }
    }

    public <T> T lookupStub() {
        try {
            Remote stub = Naming.lookup(uri);

            //noinspection unchecked
            return (T) stub;
        } catch (MalformedURLException | NotBoundException | RemoteException exception) {
            log.error("§4Cannot be lookup an endpoint uri {}: §c{}", uri, exception.toString());
        }

        return null;
    }
}
