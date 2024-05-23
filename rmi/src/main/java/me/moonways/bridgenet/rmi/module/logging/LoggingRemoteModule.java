package me.moonways.bridgenet.rmi.module.logging;

import me.moonways.bridgenet.rmi.module.AbstractRemoteModule;
import me.moonways.bridgenet.rmi.module.ModuleConst;
import me.moonways.bridgenet.rmi.module.ModuleID;
import me.moonways.bridgenet.rmi.service.ServiceInfo;

public class LoggingRemoteModule extends AbstractRemoteModule<LoggingConfig> {

    public LoggingRemoteModule() {
        super(ModuleID.of(ModuleConst.LOGGING_ID, "loggingModule"));
    }

    @Override
    public void init(ServiceInfo serviceInfo, LoggingConfig config) {

    }
}
