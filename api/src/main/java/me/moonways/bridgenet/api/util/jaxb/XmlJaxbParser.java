package me.moonways.bridgenet.api.util.jaxb;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.assembly.ResourcesAssembly;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@Autobind
public final class XmlJaxbParser {

    @Inject
    private ResourcesAssembly assembly;

    @SuppressWarnings("unchecked")
    private <X extends XmlRootObject> X parseRoot(InputStream inputStream, Class<X> cls) {
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

    public <X extends XmlRootObject> X parseResource(InputStream inputStream, Class<X> cls) {
        return parseRoot(inputStream, cls);
    }

    public <X extends XmlRootObject> X parseResource(String resourceFilepath, Class<X> cls) {
        return parseRoot(assembly.readResourceStream(resourceFilepath), cls);
    }

    public <X extends XmlRootObject> X parseResource(File file, Class<X> cls) {
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            return parseRoot(fileInputStream, cls);
        }
        catch (IOException exception) {
            log.error("§4Cannot be parse root element from {}: §c{}", cls.getName(), exception.toString());
        }
        return null;
    }

    public <X extends XmlRootObject> X parseResource(Path path, Class<X> cls) {
        return parseResource(path.toFile(), cls);
    }

    public <X extends XmlRootObject> X parseCopiedResource(String filepath, Class<X> cls) {
        Path path = Paths.get(filepath);

        if (Files.exists(path)) {
            return parseResource(path, cls);
        }

        return parseResource(filepath, cls);
    }
}
