package me.moonways.bridgenet.rsi.module;

import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XMLConfiguration;

public interface Module<Configuration extends ModuleConfiguration> {

    ModuleID getId();

    Configuration getConfig();

    void bind(XMLConfiguration instance, ServiceInfo serviceInfo, Class<Configuration> cls);
}
