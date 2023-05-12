package me.moonways.bridgenet.service.bnmg;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class BnmgFileLoader {

    private final Map<Path, BnmgFile> cached = Collections.synchronizedMap(new HashMap<>());

    private void validateNull(BnmgFile file) {
        if (file == null) {
            throw new NullPointerException("file");
        }
    }

    public void load(@NotNull BnmgFile file) {
        validateNull(file);
        cached.put(file.toPath(), file);
    }

    public void unload(@NotNull BnmgFile file) {
        validateNull(file);
        cached.remove(file.toPath());
    }

    public Collection<BnmgFile> getLoadedFiles() {
        return Collections.unmodifiableCollection(cached.values());
    }
}
