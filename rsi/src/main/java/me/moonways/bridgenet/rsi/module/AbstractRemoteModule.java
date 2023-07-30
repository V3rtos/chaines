package me.moonways.bridgenet.rsi.module;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rsi.service.ServiceException;
import me.moonways.bridgenet.rsi.service.ServiceInfo;
import me.moonways.bridgenet.rsi.xml.XmlModule;
import me.moonways.bridgenet.rsi.xml.XmlModuleProperty;
import me.moonways.bridgenet.rsi.xml.XmlConfiguration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Getter
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
    public void bind(XmlConfiguration xmlConfiguration, ServiceInfo serviceInfo, Class<Configuration> cls) {
        config = parseModuleConfiguration(xmlConfiguration, id, cls);
        init(serviceInfo, config);
    }

    public <T extends ModuleConfiguration> T parseModuleConfiguration(XmlConfiguration xmlConfiguration, ModuleID moduleID, Class<T> cls) {
        XmlModule xmlModule = xmlConfiguration.getModulesList()
                .stream()
                .filter(xml -> xml.getName().equals(moduleID.getNamespace()))
                .findFirst()
                .orElse(null);

        T config = createEmptyInstance(cls);
        List<XmlModuleProperty> properties = xmlModule.getProperties();

        for (XmlModuleProperty property : properties) {
            Class<? extends ModuleConfiguration> configClass = config.getClass();

            try {
                Field propertyField = configClass.getDeclaredField(property.getName());

                propertyField.setAccessible(true);
                propertyField.set(config, property.getValue());
            }
            catch (IllegalAccessException | NoSuchFieldException exception) {
                throw new ServiceException(exception);
            }
        }

        return config;
    }

    private <T extends ModuleConfiguration> T createEmptyInstance(Class<T> cls) {
        try {
            return cls.getConstructor().newInstance();
        }
        catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException exception) {
            throw new ServiceException(exception);
        }
    }
}
