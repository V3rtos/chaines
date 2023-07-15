package me.moonways.bridgenet.rsi.xml;

import lombok.SneakyThrows;
import me.moonways.bridgenet.rsi.module.Module;
import me.moonways.bridgenet.rsi.module.ModuleConfiguration;
import me.moonways.bridgenet.rsi.xml.element.XMLRootElement;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

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

    public <T extends ModuleConfiguration> T parseModuleConfiguration(XMLConfiguration instance, Class<T> cls) {
        return null;
    }

    public <T extends Module<?>> T parseModule(XMLConfiguration instance, Class<T> cls) {
        return null;
    }
}
