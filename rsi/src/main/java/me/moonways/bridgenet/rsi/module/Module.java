package me.moonways.bridgenet.rsi.module;

import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XMLConfigurationInstance;

public interface Module<Configuration extends ModuleConfiguration> {

    ModuleID getId();

    Configuration getConfig();

    void bind(XMLConfigurationInstance instance, ServiceInfo serviceInfo, Class<Configuration> cls);
}
