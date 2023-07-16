package me.moonways.bridgenet.rsi.xml;

import lombok.SneakyThrows;
import me.moonways.bridgenet.rsi.module.ModuleConfiguration;
import me.moonways.bridgenet.rsi.module.ModuleID;
import me.moonways.bridgenet.rsi.service.ServiceException;
import me.moonways.bridgenet.rsi.xml.element.XMLModule;
import me.moonways.bridgenet.rsi.xml.element.XMLModuleProperty;
import me.moonways.bridgenet.rsi.xml.element.XMLRootElement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class XMLConfigurationParser {

    @SneakyThrows
    private XMLRootElement parseRootElement(InputStream inputStream) {
        JAXBContext context = JAXBContext.newInstance(XMLRootElement.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();

        return (XMLRootElement) unmarshaller.unmarshal(inputStream);
    }

    public XMLConfiguration parseNewInstance() {
        InputStream resourceInput = getClass().getResourceAsStream("/rsiconfig.xml");
        XMLRootElement rootElement = parseRootElement(resourceInput);

        return new XMLConfiguration(rootElement);
    }

    public <T extends ModuleConfiguration> T parseModuleConfiguration(XMLConfiguration instance, ModuleID moduleID, Class<T> cls) {
        XMLRootElement rootElement = instance.getRootElement();
        XMLModule xmlModule = rootElement.getModulesList()
                .stream()
                .filter(xml -> xml.getName().equals(moduleID.getNamespace()))
                .findFirst()
                .orElse(null);

        T config = createEmptyInstance(cls);
        List<XMLModuleProperty> properties = xmlModule.getProperties();

        for (XMLModuleProperty property : properties) {
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
