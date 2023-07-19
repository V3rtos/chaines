package me.moonways.bridgenet.rsi.module.access;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.rsi.module.AbstractModule;
import me.moonways.bridgenet.rsi.module.ModuleConst;
import me.moonways.bridgenet.rsi.module.ModuleID;
import me.moonways.bridgenet.rsi.service.RemoteService;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;

@Log4j2
public class AccessModule extends AbstractModule<AccessConfig> {

    private static final String URI_FORMAT = "rmi://%s:%d/%s";

    private String uri;

    @Setter
    private Class<?> endpointClass;

    @Inject
    private DependencyInjection dependencyInjection;

    public AccessModule() {
        super(ModuleID.of(ModuleConst.REMOTE_ACCESS_ID, "accessModule"));
    }

    @SuppressWarnings("deprecation")
    private void injectSecurityPolicy() {
        String policyFilepath = getClass().getResource("/rmi.policy").toString();

        System.setProperty("java.security.policy", policyFilepath);
        System.setSecurityManager(new RMISecurityManager());
    }

    @Override
    public void init(ServiceInfo serviceInfo, AccessConfig config) {
        injectSecurityPolicy();

        String host = config.getRemoteHost();
        int port = serviceInfo.getPort();

        uri = String.format(URI_FORMAT, host, port, serviceInfo.getName());
    }

    public void exportStub(@NotNull ServiceInfo serviceInfo) {
        String name = serviceInfo.getName();
        Class<? extends RemoteService> subclass = endpointClass.asSubclass(RemoteService.class);

        try {
            Constructor<? extends RemoteService> constructor = subclass.getConstructor();
            RemoteService stub = constructor.newInstance();

            dependencyInjection.bind(serviceInfo.getTarget(), stub);

            try {
                LocateRegistry.createRegistry(serviceInfo.getPort());
                Naming.rebind(uri, stub);

                log.info("Endpoint '{}' was success exported: §f{}", name, stub);
            }
            catch (RemoteException exception) {
                log.error("§4Cannot be export endpoint uri {}: §c{}", uri, exception.toString());
            }
        }
        catch (Exception exception) {
            log.error("§4Cannot be allocate endpoint '{}': §c{}", name, exception.toString());
            exception.printStackTrace();
        }
    }

    public <T> T lookupStub() {
        try {
            Remote stub = Naming.lookup(uri);

            //noinspection unchecked
            return (T) stub;
        }
        catch (MalformedURLException | NotBoundException | RemoteException exception) {
            log.error("§4Cannot be lookup endpoint uri {}: §c{}", uri, exception.toString());
        }

        return null;
    }
}
