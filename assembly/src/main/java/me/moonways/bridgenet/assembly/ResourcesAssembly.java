package me.moonways.bridgenet.assembly;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.util.StreamToStringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

@Log4j2
public class ResourcesAssembly {

    @Getter
    private final ResourcesClassLoader classLoader = new ResourcesClassLoader(ResourcesAssembly.class.getClassLoader());
    @Getter
    private final ResourcesFileSystem fileSystem = new ResourcesFileSystem(this);

    /**
     * Найти и прочитать ресурс во всевозможных файловых системах
     * (локальная из ClassLoader в приоритете), и получить его в
     * виде InputStream.
     *
     * @param resourceName - наименование ресурса.
     */
    public InputStream readResourceStream(String resourceName) {
        InputStream inputStream = classLoader.readResourceStream(resourceName);
        if (inputStream != null) {
            return inputStream;
        }
        try {
            File resourceFile = fileSystem.findAsFile(resourceName);
            return new FileInputStream(resourceFile);
        }
        catch (FileNotFoundException exception) {
            return null;
        }
    }

    /**
     * Найти и прочитать ресурс во всевозможных файловых системах
     * (локальная из ClassLoader в приоритете), и получить его в
     * виде строкового пути URI.
     *
     * @param resourceName - наименование ресурса.
     */
    public String readResourcePath(String resourceName) {
        URL url = classLoader.readResourceURL(resourceName);
        if (url != null) {
            return url.toString();
        }

        File resourceFile = fileSystem.findAsFile(resourceName);
        return resourceFile.toURI().toString();
    }

    /**
     * Прочитать и получить полное содержание ресурса
     * как файла в виде строки.
     *
     * @param resourceName - наименование ресурса.
     * @param charset - кодировка, в которой воспроизводить чтение.
     */
    public String readResourceFullContent(String resourceName, Charset charset) {
        return StreamToStringUtils.toStringFull(readResourceStream(resourceName), charset);
    }

    /**
     * Прочитать и получить полное содержание ресурса
     * как файла в виде строки.
     *
     * @param resourceName - наименование ресурса.
     */
    public String readResourceFullContent(String resourceName) {
        return StreamToStringUtils.toStringFull(readResourceStream(resourceName));
    }

    /**
     * Проверить на существование ресурса в
     * локальной файловой системе ClassLoader.
     *
     * @param resourceName - наименование ресурса.
     */
    @SuppressWarnings("resource")
    public boolean hasInClassLoader(String resourceName) {
        return classLoader.readResourceStream(resourceName) != null;
    }
}
