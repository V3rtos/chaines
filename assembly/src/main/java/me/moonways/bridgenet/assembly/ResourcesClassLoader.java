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

    /**
     * Найти и прочитать локальный ресурс, который
     * находится в ClassLoader модуля сборки системы
     * Bridgenet, и прочитать его в виде URI.
     *
     * @param resourceName - наименование ресурса.
     */
    public URI readResourceURI(String resourceName) {
        try {
            return readResourceURL(resourceName).toURI();
        } catch (URISyntaxException exception) {
            throw new BridgenetAssemblyException("can not read a resource as uri", exception);
        }
    }

    /**
     * Найти и прочитать локальный ресурс, который
     * находится в ClassLoader модуля сборки системы
     * Bridgenet, и прочитать его в виде URL.
     *
     * @param resourceName - наименование ресурса.
     */
    public URL readResourceURL(String resourceName) {
        return classLoader.getResource(correctlyName(resourceName));
    }

    /**
     * Найти и прочитать локальный ресурс, который
     * находится в ClassLoader модуля сборки системы
     * Bridgenet, и прочитать его в виде InputStream.
     *
     * @param resourceName - наименование ресурса.
     */
    public InputStream readResourceStream(String resourceName) {
        return classLoader.getResourceAsStream(correctlyName(resourceName));
    }

    /**
     * Скорректировать наименование ресурса для
     * воспроизведения более быстрого поиска его
     * в локальной файловой системе ClassLoader.
     *
     * @param resourceName - наименование ресурса.
     */
    private String correctlyName(String resourceName) {
        if (resourceName.startsWith(RESOURCE_NAME_PREFIX))
            return resourceName;
        return RESOURCE_NAME_PREFIX + resourceName;
    }
}
