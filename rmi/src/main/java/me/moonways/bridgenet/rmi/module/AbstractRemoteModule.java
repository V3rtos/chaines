package me.moonways.bridgenet.rmi.module;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
import me.moonways.bridgenet.rmi.service.RemoteServiceException;
import me.moonways.bridgenet.rmi.service.ServiceInfo;
import me.moonways.bridgenet.rmi.xml.XMLServiceModuleDescriptor;
import me.moonways.bridgenet.rmi.xml.XMLServiceModulePropertyDescriptor;
import me.moonways.bridgenet.rmi.xml.XMLServicesConfigDescriptor;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Getter
@Log4j2
@ToString(onlyExplicitlyIncluded = true)
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class AbstractRemoteModule<Configuration extends ModuleConfiguration>
        implements RemoteModule<Configuration> {

    @ToString.Include
    private final ModuleID id;

    private Configuration config;

    public abstract void init(ServiceInfo serviceInfo, Configuration config);

    @Override
    public void bind(XMLServicesConfigDescriptor xmlConfiguration, ServiceInfo serviceInfo, Class<Configuration> cls) {
        config = parseModuleConfiguration(xmlConfiguration, id, cls);
        init(serviceInfo, config);
    }

    public <T extends ModuleConfiguration> T parseModuleConfiguration(XMLServicesConfigDescriptor xmlConfiguration, ModuleID moduleID, Class<T> cls) {
        XMLServiceModuleDescriptor xmlModule = xmlConfiguration.getModulesList()
                .stream()
                .filter(xml -> xml.getName().equals(moduleID.getNamespace()))
                .findFirst()
                .orElse(null);

        T config = createEmptyInstance(cls);
        List<XMLServiceModulePropertyDescriptor> properties = xmlModule.getProperties();

        for (XMLServiceModulePropertyDescriptor property : properties) {
            Class<? extends ModuleConfiguration> configClass = config.getClass();

            try {
                Field propertyField = configClass.getDeclaredField(property.getName());

                ReflectionUtils.grantAccess(propertyField);
                propertyField.set(config, property.getValue());
            } catch (IllegalAccessException | NoSuchFieldException exception) {
                log.error(new RemoteServiceException(exception));
            }
        }

        return config;
    }

    private <T extends ModuleConfiguration> T createEmptyInstance(Class<T> cls) {
        try {
            return cls.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException exception) {
            log.error(new RemoteServiceException(exception));
            return null;
        }
    }
}
