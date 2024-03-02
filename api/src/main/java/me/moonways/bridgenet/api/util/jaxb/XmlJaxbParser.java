package me.moonways.bridgenet.api.util.jaxb;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.assembly.ResourcesAssembly;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;

@Log4j2
public final class XmlJaxbParser {

    @Inject
    private ResourcesAssembly assembly;

    @SuppressWarnings("unchecked")
    private <X extends XmlRootObject> X parseInputStream(InputStream inputStream, Class<X> cls) {
        try {
            JAXBContext context = JAXBContext.newInstance(cls);
            Unmarshaller unmarshaller = context.createUnmarshaller();

            return (X) unmarshaller.unmarshal(inputStream);
        }
        catch (JAXBException exception) {
            log.error("§4Cannot be parse root element from {}: §c{}", cls.getName(), exception.toString());
        }

        return null;
    }

    public <X extends XmlRootObject> X parseToDescriptorByType(InputStream inputStream, Class<X> cls) {
        return parseInputStream(inputStream, cls);
    }

    public <X extends XmlRootObject> X parseToDescriptorByType(String resourceFilepath, Class<X> cls) {
        return parseToDescriptorByType(assembly.readResourceStream(resourceFilepath), cls);
    }
}
