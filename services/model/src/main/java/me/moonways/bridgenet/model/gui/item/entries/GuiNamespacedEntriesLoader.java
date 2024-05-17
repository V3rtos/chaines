package me.moonways.bridgenet.model.gui.item.entries;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.IgnoredRegistry;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.assembly.ResourcesAssembly;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Log4j2
@IgnoredRegistry
@RequiredArgsConstructor
public final class GuiNamespacedEntriesLoader<T extends ItemsEntry> {

    private static final Gson GSON = new Gson();
    private static final String DATA_FOLDER = "minecraft_data/";

    private final String resourceName;
    private final Class<T> entryClass;

    @Inject
    private ResourcesAssembly assembly;

    public void load(List<GuiNamespacedEntry<T>> entries) {
        InputStream inputStream = assembly.readResourceStream(DATA_FOLDER + resourceName);
        JsonElement jsonElement = JsonParser.parseReader(
                new JsonReader(
                        new InputStreamReader(inputStream)));

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        for (GuiNamespacedEntry<T> entry : entries) {
            log.debug("Loading namespaced entry §7\"{}\" §rfor §6{}", entry.getNamespace(), entry.getInstance().getClass().getName());

            JsonObject entryJsonObject = jsonObject.getAsJsonObject(entry.getNamespace());

            T completed = GSON.fromJson(entryJsonObject.toString(), entryClass);

            entry.getInstance().set(completed);
        }
    }
}
