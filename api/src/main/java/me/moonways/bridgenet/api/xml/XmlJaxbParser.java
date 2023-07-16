package me.moonways.bridgenet.api.xml;

import lombok.extern.log4j.Log4j2;

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
public final class XmlJaxbParser {

    private String correctLocalFilepathName(String filepath) {
        String prefix = "/";
        if (filepath.startsWith(prefix)) {
            return filepath;
        }
        return prefix.concat(filepath);
    }

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

    public <X extends XmlRootObject> X parseResource(ClassLoader classLoader, String resourceFilepath, Class<X> cls) {
        String filepathName = correctLocalFilepathName(resourceFilepath);
        return parseRoot(classLoader.getClass().getResourceAsStream(filepathName), cls);
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

    public <X extends XmlRootObject> X parseCopiedResource(ClassLoader classLoader, String filepath, Class<X> cls) {
        Path path = Paths.get(filepath);

        if (Files.exists(path)) {
            return parseResource(path, cls);
        }

        return parseResource(classLoader, filepath, cls);
    }
}
