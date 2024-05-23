package me.moonways.bridgenet.rmi.module;

import me.moonways.bridgenet.rmi.service.ServiceInfo;
import me.moonways.bridgenet.rmi.xml.XMLServicesConfigDescriptor;

public interface RemoteModule<Configuration extends ModuleConfiguration> {

    ModuleID getId();

    Configuration getConfig();

    void bind(XMLServicesConfigDescriptor xmlConfiguration, ServiceInfo serviceInfo, Class<Configuration> cls);
}
