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

    private static final String EXCLUDE_DIRECTORY_NAME = (File.separator + "test-engine");
    private static final String ETC_DIRECTORY_PREFIX = "etc";

    private final ResourcesAssembly assembly;

    private Path findPath(String resourceName) {
        Path etcDirectoryPath = Paths.get("assembly", ETC_DIRECTORY_PREFIX);
        if (!Files.exists(etcDirectoryPath)) {
            etcDirectoryPath = Paths.get(ETC_DIRECTORY_PREFIX);
        }

        Path result = etcDirectoryPath.resolve(resourceName);
        return !Files.exists(result) ? findPathWithExclude(resourceName): result;
    }

    private Path findPathWithExclude(String resourceName) {
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

    public void copy(String resourceName) {
        Path path = findPath(resourceName);
        if (!Files.exists(path)) {
            try {
                Files.copy(assembly.readResourceStream(resourceName), path);
            } catch (IOException exception) {
                log.error("§4Couldn't copy resource {} to 'etc' directory", resourceName);
            }
        }
    }

    public File findAsFile(String resourceName) {
        Path path = findPath(resourceName);
        if (!Files.exists(path)) {
            System.out.println(path.toAbsolutePath());
            log.warn("§eCouldn't find resource {} in 'etc' directory", resourceName);
        }
        return path.toFile();
    }
}
