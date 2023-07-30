package me.moonways.bridgenet.rsi.module.logging;

import me.moonways.bridgenet.rsi.module.AbstractRemoteModule;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.module.ModuleID;
import me.moonways.bridgenet.rsi.module.ModuleConst;

public class LoggingRemoteModule extends AbstractRemoteModule<LoggingConfig> {

    public LoggingRemoteModule() {
        super(ModuleID.of(ModuleConst.LOGGING_ID, "loggingModule"));
    }

    @Override
    public void init(ServiceInfo serviceInfo, LoggingConfig config) {

    }
}
