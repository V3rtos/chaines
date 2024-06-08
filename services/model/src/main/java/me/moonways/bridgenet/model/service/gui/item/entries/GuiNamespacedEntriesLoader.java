package me.moonways.bridgenet.model.service.gui.item.entries;

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
import org.jetbrains.annotations.NotNull;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Log4j2
@IgnoredRegistry
@RequiredArgsConstructor
public final class GuiNamespacedEntriesLoader<T extends ItemsEntry> {

    private static final JsonParser JSON_PARSER = new JsonParser();
    private static final Gson GSON = new Gson();

    private final String resourceName;
    private final Class<T> entryClass;

    @Inject
    private ResourcesAssembly assembly;

    public void load(@NotNull List<GuiNamespacedEntry<T>> entries) {
        InputStream inputStream = assembly.readResourceStream(resourceName);
        JsonElement jsonElement = JSON_PARSER.parse(
                new JsonReader(
                        new InputStreamReader(inputStream)));

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        String instanceName = null;

        for (GuiNamespacedEntry<T> entry : entries) {
            if (instanceName == null) {
                instanceName = entry.getInstance().getClass().getName();
            }

            JsonObject entryJsonObject = jsonObject.getAsJsonObject(entry.getNamespace());
            T completed = GSON.fromJson(entryJsonObject.toString(), entryClass);

            entry.getInstance().set(completed);
        }

        log.debug("Success loaded §2{} §rnamespaced entries from §6{}", entries.size(), instanceName);
    }
}
