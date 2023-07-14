package me.moonways.bridgenet.rsi.module.access;

import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.module.ModuleID;
import me.moonways.bridgenet.rsi.module.AbstractModule;
import me.moonways.bridgenet.rsi.module.ModuleConsts;

public class AccessModule extends AbstractModule<AccessConfig> {

    private static final String URI_FORMAT = "bridgenet:rmi://%s:%d/%s";

    private String host;
    private int port;

    public AccessModule() {
        super(ModuleID.of(ModuleConsts.REMOTE_ACCESS_ID, "remote_access"));
    }

    @Override
    public void init(ServiceInfo serviceInfo, AccessConfig config) {
        host = config.getHost();
        port = serviceInfo.getPort();
    }

    public final String createFullUri(String name) {
        return String.format(URI_FORMAT, host, port, name);
    }

    public void export(ServiceInfo serviceInfo) {
    }

    public void lookup(ServiceInfo serviceInfo) {
    }
}
