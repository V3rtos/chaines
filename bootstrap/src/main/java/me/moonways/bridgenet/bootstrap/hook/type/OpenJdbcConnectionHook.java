package me.moonways.bridgenet.bootstrap.hook.type;

import com.google.gson.Gson;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.core.observer.ObserverAdapter;
import me.moonways.bridgenet.jdbc.core.observer.event.*;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.jdbc.provider.BridgenetJdbcProvider;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.metrics.BridgenetMetricsLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class OpenJdbcConnectionHook extends ApplicationBootstrapHook {

    @Inject
    private BeansService beansService;
    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private BridgenetMetricsLogger bridgenetMetricsLogger;
    @Inject
    private Gson gson;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        BridgenetJdbcProvider.JdbcSettingsConfig jdbcSettingsConfig = readSettings();

        BridgenetJdbcProvider bridgenetJdbcProvider = new BridgenetJdbcProvider();
        bridgenetJdbcProvider.initConnection(jdbcSettingsConfig);

        handleDatabaseEventsForMetric(bridgenetJdbcProvider.getDatabaseConnection());

        bindAllBeans(bridgenetJdbcProvider);
    }

    private void bindAllBeans(BridgenetJdbcProvider bridgenetJdbcProvider) {
        DatabaseProvider databaseProvider = bridgenetJdbcProvider.getDatabaseProvider();
        DatabaseConnection databaseConnection = bridgenetJdbcProvider.getDatabaseConnection();
        DatabaseComposer composer = databaseProvider.getComposer();

        beansService.bind(databaseProvider);
        beansService.bind(composer);
        beansService.bind(databaseConnection);
        beansService.bind(new EntityRepositoryFactory(composer, databaseConnection));
    }

    private BridgenetJdbcProvider.JdbcSettingsConfig readSettings() {
        return gson.fromJson(
                assembly.readResourceFullContent(
                        ResourcesTypes.JDBC_SETTINGS_JSON,
                        StandardCharsets.UTF_8),
                BridgenetJdbcProvider.JdbcSettingsConfig.class);
    }

    private void handleDatabaseEventsForMetric(DatabaseConnection connection) {
        connection.addObserver(new ObserverAdapter() {

            @Override
            protected void observe(DbRequestCompletedEvent event) {
                bridgenetMetricsLogger.logDatabaseSuccessQuery();
            }

            @Override
            protected void observe(DbRequestFailureEvent event) {
                bridgenetMetricsLogger.logDatabaseFailureQuery();
            }

            @Override
            protected void observe(DbTransactionOpenEvent event) {
                bridgenetMetricsLogger.logDatabaseTransaction();
            }

            @Override
            protected void observe(DbTransactionRollbackEvent event) {
                bridgenetMetricsLogger.logDatabaseRollbackQuery();
            }
        });
    }
}
