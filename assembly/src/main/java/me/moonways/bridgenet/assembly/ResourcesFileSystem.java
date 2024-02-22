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

    private final ResourcesAssembly assembly;

    public void copy(String resourceName) {
        Path path = Paths.get(resourceName);
        if (!Files.exists(path)) {
            try {
                Files.copy(assembly.readResourceStream(resourceName), path);
            } catch (IOException exception) {
                log.error("§4Couldn't copy resource {} to general directory", resourceName);
            }
        }
    }

    public File findAsFile(String resourceName) {
        Path path = Paths.get(resourceName);
        if (!Files.exists(path)) {
            log.warn("§eCouldn't find resource {} in general directory", resourceName);
        }
        return path.toFile();
    }
}
