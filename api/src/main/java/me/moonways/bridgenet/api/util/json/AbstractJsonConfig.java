package me.moonways.bridgenet.api.util.json;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.ResourcesAssembly;

@Log4j2
@RequiredArgsConstructor
public abstract class AbstractJsonConfig<O> {

    private static final ResourcesAssembly RESOURCES_ASSEMBLY = new ResourcesAssembly();
    private static final Gson GSON = new Gson();

    private final Class<O> sourceType;
    private final String filename;

    @Synchronized
    public void reload() {
        String configurationContent = RESOURCES_ASSEMBLY.readResourceFullContent(filename);

        O object = GSON.fromJson(configurationContent, sourceType);
        doReload(object);

        log.info("Json configuration parsed from {}", filename);
    }

    protected abstract void doReload(O object);
}
