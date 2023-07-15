package me.moonways.bridgenet.injection.xml;

import lombok.SneakyThrows;
import me.moonways.bridgenet.injection.xml.element.XMLRootElement;

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
        InputStream resourceInput = getClass().getResourceAsStream("/injectconfig.xml");
        XMLRootElement rootElement = parseRootElement(resourceInput);

        return new XMLConfiguration(rootElement);
    }
}
