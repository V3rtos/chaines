package me.moonways.bridgenet.rsi.module;

import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XmlConfiguration;

public interface Module<Configuration extends ModuleConfiguration> {

    ModuleID getId();

    Configuration getConfig();

    void bind(XmlConfiguration xmlConfiguration, ServiceInfo serviceInfo, Class<Configuration> cls);
}
