package me.moonways.service.bnmg;

import lombok.experimental.Delegate;
import me.moonways.service.bnmg.descriptor.GuiDescriptor;
import me.moonways.bridgenet.injection.Component;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Component
public final class BnmgServiceImpl {

    public static final String FILE_NAME_FORMAT = ".bnmg";

    @Delegate
    private final BnmgFileLoader fileLoader = new BnmgFileLoader();

    private final Map<String, GuiDescriptor> descriptorMap = Collections.synchronizedMap(new HashMap<>());

    private void validateNull(File file) {
        if (file == null) {
            throw new NullPointerException("file");
        }
    }

    private void validateNull(Path path) {
        if (path == null) {
            throw new NullPointerException("path");
        }
    }

    private void validateFileFormat(File file) {
        if (!file.getName().endsWith(FILE_NAME_FORMAT)) {
            throw new IllegalArgumentException(String.format("file %s format is not equal of %s", file.getName(), FILE_NAME_FORMAT));
        }
    }

    public BnmgFile convert(@NotNull File file) {
        validateNull(file);
        validateFileFormat(file);

        return new BnmgFile(file.getPath());
    }

    public BnmgFile convert(@NotNull Path path) {
        validateNull(path);
        return convert(path.toFile());
    }

    public void findResources() {
        // todo
    }
}
