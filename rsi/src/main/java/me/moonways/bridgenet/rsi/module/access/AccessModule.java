package me.moonways.bridgenet.rsi.module.access;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.rsi.module.AbstractModule;
import me.moonways.bridgenet.rsi.module.ModuleConsts;
import me.moonways.bridgenet.rsi.module.ModuleID;
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

    public AccessModule() {
        super(ModuleID.of(ModuleConsts.REMOTE_ACCESS_ID, "accessModule"));
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
        Class<? extends Remote> subclass = endpointClass.asSubclass(Remote.class);

        try {
            Constructor<? extends Remote> constructor = subclass.getConstructor();
            Remote object = constructor.newInstance();

            try {
                LocateRegistry.createRegistry(serviceInfo.getPort());
                Naming.rebind(uri, object);

                log.info("Endpoint '{}' was success exported: §f{}", name, object);
            }
            catch (RemoteException exception) {
                log.error("§4Cannot be export endpoint uri {}: §f{}", uri, exception.toString());
            }
        }
        catch (Exception exception) {
            log.error("§4Cannot be allocate endpoint '{}': §c{}", name, exception.toString());
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
