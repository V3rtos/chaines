package me.moonways.bridgenet.bootstrap.hook.type;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.jdbc.provider.BridgenetJdbcProvider;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class OpenJdbcConnectionHook extends ApplicationBootstrapHook {

    @Inject
    private BeansService beansService;
    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private Gson gson;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        BridgenetJdbcProvider.JdbcSettingsConfig jdbcSettingsConfig = readSettings();

        BridgenetJdbcProvider bridgenetJdbcProvider = new BridgenetJdbcProvider();

        bridgenetJdbcProvider.initConnection(jdbcSettingsConfig);

        beansService.bind(bridgenetJdbcProvider.getDatabaseProvider());
        beansService.bind(bridgenetJdbcProvider.getDatabaseProvider().getComposer());
        beansService.bind(bridgenetJdbcProvider.getDatabaseConnection());
    }

    private BridgenetJdbcProvider.JdbcSettingsConfig readSettings() {
        return gson.fromJson(
                assembly.readResourceFullContent(
                        ResourcesTypes.JDBC_SETTINGS_JSON,
                        StandardCharsets.UTF_8),
                BridgenetJdbcProvider.JdbcSettingsConfig.class);
    }
}
