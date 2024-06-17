package me.moonways.bridgenet.bootstrap.system;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.BridgenetAssemblyException;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Log4j2
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OverridenPropertiesManager {

    public static final OverridenPropertiesManager INSTANCE = new OverridenPropertiesManager();
    private static final ResourcesAssembly ASSEMBLY = new ResourcesAssembly();

    private Properties overridenProperties;

    /**
     * Подгрузить и перезаписать данные в системные properties
     * из отдельной конфигурации сборки 'config.properties'
     */
    public void run() {
        InputStream propertiesStream = ASSEMBLY.readResourceStream(ResourcesTypes.SYSTEM_OVERRIDE_PROPERTIES);
        Properties properties = new Properties();

        try {
            properties.load(propertiesStream);
            overridenProperties = properties;
        } catch (IOException exception) {
            throw new BridgenetAssemblyException(exception);
        }

        overridenProperties.forEach((propertyName, value) ->
                System.setProperty(propertyName.toString(), value.toString()));

        log.info("config.properties content has overriden");
    }
}
