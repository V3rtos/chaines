package me.moonways.bridgenet.rsi.module.logging;

import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.module.ModuleID;
import me.moonways.bridgenet.rsi.module.AbstractModule;
import me.moonways.bridgenet.rsi.module.ModuleConsts;

public class LoggingModule extends AbstractModule<LoggingConfig> {

    public LoggingModule() {
        super(ModuleID.of(ModuleConsts.LOGGING_ID, "loggingModule"));
    }

    @Override
    public void init(ServiceInfo serviceInfo, LoggingConfig config) {

    }
}
