package me.moonways.bridgenet.assembly;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.ini.IniConfig;
import me.moonways.bridgenet.assembly.ini.IniConfigLoader;
import me.moonways.bridgenet.assembly.jaxb.XmlJaxbParser;
import me.moonways.bridgenet.assembly.jaxb.XmlRootObject;
import me.moonways.bridgenet.assembly.util.StreamToStringUtils;

import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;

@Log4j2
public final class ResourcesAssembly {

    private static final Gson GSON = new GsonBuilder()
            .setLenient()
            .create();

    @Getter
    private final ResourcesClassLoader classLoader = new ResourcesClassLoader(ResourcesAssembly.class.getClassLoader());
    @Getter
    private final ResourcesFileSystem fileSystem = new ResourcesFileSystem(this);
    @Getter
    private final XmlJaxbParser xmlJaxbParser = new XmlJaxbParser(this);
    @Getter
    private final IniConfigLoader iniConfigLoader = new IniConfigLoader();

    /**
     * Создать новый пустой по содержанию ресурс
     * в общем каталоге ресурсов `etc` в локальной
     * версии запущенной системы.
     *
     * @param resourceName - наименование создаваемого ресурса
     */
    public void createEmptyResource(String resourceName) {
        log.debug("§7createEmptyResource(resourceName={})", resourceName);
        File emptyFile = fileSystem.createEmptyFile(resourceName);

        if (emptyFile == null) {
            throw new BridgenetAssemblyException("create: " + resourceName);
        }
    }

    /**
     * Скопировать ресурс из локальной файловой системы
     * (ClassLoader) в общий каталог ресурсов `etc` в локальной
     * версии запущенной системы.
     *
     * @param resourceName - наименование создаваемого ресурса
     * @param createOnFailure - воспроизвести попытку создания пустого файла в случае неудачи копирования
     */
    public void copyLocalResource(String resourceName, boolean createOnFailure) {
        log.debug("§7copyLocalResource(resourceName={}, createOnFailure={})", resourceName, createOnFailure);
        boolean isCopied = fileSystem.copy(resourceName);

        if (!isCopied && createOnFailure) {
            createEmptyResource(resourceName);
        }
    }

    /**
     * Скопировать ресурс из локальной файловой системы
     * (ClassLoader) в общий каталог ресурсов `etc` в локальной
     * версии запущенной системы.
     *
     * @param resourceName - наименование создаваемого ресурса
     */
    public void copyLocalResource(String resourceName) {
        log.debug("§7copyLocalResource(resourceName={})", resourceName);
        fileSystem.copy(resourceName);
    }

    /**
     * Найти и прочитать ресурс во всевозможных файловых системах
     * (локальная из ClassLoader в приоритете), и получить его в
     * виде InputStream.
     *
     * @param resourceName - наименование ресурса.
     */
    public InputStream readResourceStream(String resourceName) {
        log.debug("§7readResourceStream(resourceName={})", resourceName);

        InputStream inputStream = classLoader.readResourceStream(resourceName);
        if (inputStream != null) {
            return inputStream;
        }
        try {
            File resourceFile = fileSystem.findAsFile(resourceName);
            return new FileInputStream(resourceFile);
        } catch (FileNotFoundException exception) {
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
        log.debug("§7readResourcePath(resourceName={})", resourceName);

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
     * @param charset      - кодировка, в которой воспроизводить чтение.
     */
    public String readResourceFullContent(String resourceName, Charset charset) {
        log.debug("§7readResourceFullContent(resourceName={}, charset={})", resourceName, charset);
        return StreamToStringUtils.toStringFull(readResourceStream(resourceName), charset);
    }

    /**
     * Прочитать и получить полное содержание ресурса
     * как файла в виде строки.
     *
     * @param resourceName - наименование ресурса.
     */
    public String readResourceFullContent(String resourceName) {
        log.debug("§7readResourceFullContent(resourceName={})", resourceName);
        return StreamToStringUtils.toStringFull(readResourceStream(resourceName));
    }

    /**
     * Прочитать полное содержание ресурса, спарсить
     * полученный текст как json и создать по его шаблону
     * и типа класса сущности объект.
     *
     * @param resourceName - наименование ресурса.
     * @param charset      - кодировка, в которой воспроизводить чтение.
     * @param entity       - тип сущности, в которую преобразовывать полученный json.
     */
    public <T> T readJsonAtEntity(String resourceName, Charset charset, Class<T> entity) {
        log.debug("§7readJsonAtEntity(resourceName={}, charset={}, entity={})", resourceName, charset, entity.getName());
        return GSON.fromJson(readResourceFullContent(resourceName, charset), entity);
    }

    /**
     * Прочитать полное содержание ресурса, спарсить
     * полученный текст как json и создать по его шаблону
     * и типа класса сущности объект.
     *
     * @param resourceName - наименование ресурса.
     * @param entity       - тип сущности, в которую преобразовывать полученный json.
     */
    public <T> T readJsonAtEntity(String resourceName, Class<T> entity) {
        log.debug("§7readJsonAtEntity(resourceName={}, entity={})", resourceName, entity.getName());
        return GSON.fromJson(readResourceFullContent(resourceName), entity);
    }

    /**
     * Прочитать полное содержание ресурса, спарсить
     * полученный текст как XML и создать по его шаблону
     * и типа класса сущности объект.
     *
     * @param resourceName - наименование ресурса.
     * @param entity       - тип сущности, в которую преобразовывать полученный XML.
     */
    public <T extends XmlRootObject> T readXmlAtEntity(String resourceName, Class<T> entity) {
        log.debug("§7readXmlAtEntity(resourceName={}, entity={})", resourceName, entity.getName());
        return xmlJaxbParser.parseToDescriptorByType(readResourceStream(resourceName), entity);
    }

    /**
     * Прочитать полное содержание ресурса и спарсить
     * полученный текст как INI конфигурация
     *
     * @param resourceName - наименование ресурса.
     */
    public IniConfig readIniConfig(String resourceName) {
        log.debug("§7readIniConfig(resourceName={})", resourceName);
        return iniConfigLoader.load(readResourceStream(resourceName));
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
