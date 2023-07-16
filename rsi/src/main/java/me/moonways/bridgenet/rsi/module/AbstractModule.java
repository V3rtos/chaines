package me.moonways.bridgenet.rsi.module;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XMLConfiguration;
import me.moonways.bridgenet.rsi.xml.XMLConfigurationParser;

@Getter
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class AbstractModule<Configuration extends ModuleConfiguration>
        implements Module<Configuration> {

    private static final XMLConfigurationParser XML_CONFIGURATION_PARSER = new XMLConfigurationParser();

    @ToString.Include
    private final ModuleID id;

    private Configuration config;

    public abstract void init(ServiceInfo serviceInfo, Configuration config);

    @Override
    public void bind(XMLConfiguration instance, ServiceInfo serviceInfo, Class<Configuration> cls) {
        config = XML_CONFIGURATION_PARSER.parseModuleConfiguration(instance, id, cls);
        init(serviceInfo, config);
    }
}
