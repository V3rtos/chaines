package me.moonways.bridgenet.metrics.settings;

import com.google.gson.Gson;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;

public final class MetricsSettingsParser {

    private static final ResourcesAssembly ASSEMBLY = new ResourcesAssembly();
    private static final Gson GSON = new Gson();

    public MetricsSettings readSettings() {
        return GSON.fromJson(
                ASSEMBLY.readResourceFullContent(
                        ResourcesTypes.METRICS_SETTINGS_JSON),
                MetricsSettings.class);
    }
}
