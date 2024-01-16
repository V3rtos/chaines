package me.moonways.bridgenet.rsi.module;

import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XMLServicesConfigDescriptor;

public interface RemoteModule<Configuration extends ModuleConfiguration> {

    ModuleID getId();

    Configuration getConfig();

    void bind(XMLServicesConfigDescriptor xmlConfiguration, ServiceInfo serviceInfo, Class<Configuration> cls);
}
