package me.moonways.bridgenet.assembly;

import lombok.RequiredArgsConstructor;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

@RequiredArgsConstructor
public class ResourcesClassLoader {

    private static final String RESOURCE_NAME_PREFIX = "/";

    private final ClassLoader classLoader;

    public URI readResourceURI(String resourceName) {
        try {
            return readResourceURL(resourceName).toURI();
        } catch (URISyntaxException exception) {
            throw new BridgenetAssemblyException("can not read a resource as uri", exception);
        }
    }

    public URL readResourceURL(String resource) {
        return classLoader.getResource(correctlyName(resource));
    }

    public InputStream readResourceStream(String resource) {
        return classLoader.getResourceAsStream(correctlyName(resource));
    }

    private String correctlyName(String resourceName) {
        if (resourceName.startsWith(RESOURCE_NAME_PREFIX))
            return resourceName;
        return RESOURCE_NAME_PREFIX + resourceName;
    }
}
