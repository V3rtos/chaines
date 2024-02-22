package me.moonways.bridgenet.assembly;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.util.StreamToStringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.function.Function;

@Log4j2
public class ResourcesAssembly {

    private static final String REQUIRED_RESOURCES_DIR = "required/";

    private static final String LOCAL_RESOURCES_DIR = "local/";

    private final ResourcesClassLoader classLoader = new ResourcesClassLoader(ResourcesAssembly.class.getClassLoader());
    private final ResourcesFileSystem fileSystem = new ResourcesFileSystem(this);

    public InputStream readResourceStream(String resourceName) {
        return readResourceTotals(resourceName, classLoader::readResourceStream);
    }

    public URL readResourceURL(String resourceName) {
        return readResourceTotals(resourceName, classLoader::readResourceURL);
    }

    public String readResourcePath(String resourceName) {
        return readResourceURL(resourceName).toString();
    }

    public String readResourceFullContent(String resourceName, Charset charset) {
        return StreamToStringUtil.toStringFull(readResourceStream(resourceName), charset);
    }

    public String readResourceFullContent(String resourceName) {
        return StreamToStringUtil.toStringFull(readResourceStream(resourceName));
    }

    private <T> T readResourceTotals(String resourceName, Function<String, T> read) {
        T result = read.apply(resourceName);
        if (result == null) {
            result = read.apply(REQUIRED_RESOURCES_DIR + resourceName);
            if (result == null) {
                result = read.apply(LOCAL_RESOURCES_DIR + resourceName);
            }
        }
        return result;
    }
}
