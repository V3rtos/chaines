package me.moonways.bridgenet.assembly;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Log4j2
@RequiredArgsConstructor
public final class ResourcesFileSystem {

    private static final String EXCLUDE_DIRECTORY_NAME = (File.separator + "testing" + File.separator + "units");
    private static final String ETC_DIRECTORY_PREFIX = "etc";

    private final ResourcesAssembly assembly;

    /**
     * Найти путь к ресурсу по его имени в файловой
     * системе сборки Bridgenet.
     *
     * @param resourceName - наименование ресурса.
     */
    private Path findPath(String resourceName) {
        resourceName = resourceName.replace("/", File.separator);
        Path etcDirectoryPath = Paths.get("assembly", ETC_DIRECTORY_PREFIX);

        if (!Files.exists(etcDirectoryPath)) {
            etcDirectoryPath = Paths.get(ETC_DIRECTORY_PREFIX);
        }

        Path result = etcDirectoryPath.resolve(resourceName);
        return !Files.exists(result) ? findPathWithExclude(resourceName) : result;
    }

    /**
     * Найти путь к файлу или директории в общей папке проекта.
     *
     * @param resourceName - наименования поискового ресурса.
     */
    public Path findPathAtProject(String resourceName) {
        resourceName = resourceName.replace("/", File.separator);
        String rootPathname = Paths.get("").toAbsolutePath().toString();

        if (rootPathname.contains(EXCLUDE_DIRECTORY_NAME)) {
            rootPathname = rootPathname.replace(EXCLUDE_DIRECTORY_NAME, "");
        }

        String absolutePathname = rootPathname + File.separator + resourceName;
        return new File(absolutePathname).toPath();
    }

    /**
     * Найти путь к ресурсу по его имени в файловой
     * системе сборки Bridgenet, учитывая и сравнивая
     * родительские директории с теми, что находится в EXCLUDE
     * и обрезая их, давая возможность найти необходимый
     * нам ресурс в любом случае.
     *
     * @param resourceName - наименование ресурса.
     */
    private Path findPathWithExclude(String resourceName) {
        resourceName = resourceName.replace("/", File.separator);
        String rootPathname = Paths.get("").toAbsolutePath().toString();

        if (rootPathname.contains(EXCLUDE_DIRECTORY_NAME)) {
            rootPathname = rootPathname.replace(EXCLUDE_DIRECTORY_NAME, "");
        }

        Path etcFilepath = Paths.get("assembly", ETC_DIRECTORY_PREFIX);
        String absolutePathname = rootPathname + File.separator + etcFilepath.resolve(resourceName);

        if (!Files.exists(new File(absolutePathname).toPath())) {
            etcFilepath = Paths.get(ETC_DIRECTORY_PREFIX);
            absolutePathname = rootPathname + File.separator + etcFilepath.resolve(resourceName);
        }

        return new File(absolutePathname).toPath();
    }

    /**
     * Скопировать локальный ресурс из ClassLoader в
     * общую директорию ресурсов.
     *
     * @param resourceName - наименование ресурса.
     */
    public void copy(String resourceName) {
        Path path = findPath(resourceName);
        if (!Files.exists(path)) {
            try {
                Files.copy(assembly.readResourceStream(resourceName), path);
            } catch (IOException exception) {
                log.error("§4Couldn't copy resource \"{}\" to '{}' directory", resourceName, path);
            }
        }
    }

    /**
     * Воспроизвести поиск ресурса как файла
     * в файловой системе сборки Bridgenet.
     *
     * @param resourceName - наименование ресурса.
     */
    public File findAsFile(String resourceName) {
        Path path = findPath(resourceName);
        if (!Files.exists(path)) {
            log.warn("§6Couldn't find resource \"{}\" in '{}' directory", resourceName, path);
        }
        return path.toFile();
    }
}
