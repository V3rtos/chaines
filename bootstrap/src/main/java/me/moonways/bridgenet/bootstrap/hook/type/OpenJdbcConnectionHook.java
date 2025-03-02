package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.assembly.ResourcesAssembly;
import me.moonways.bridgenet.assembly.ResourcesTypes;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.core.observer.ObserverAdapter;
import me.moonways.bridgenet.jdbc.core.observer.event.DbRequestCompletedEvent;
import me.moonways.bridgenet.jdbc.core.observer.event.DbRequestFailureEvent;
import me.moonways.bridgenet.jdbc.core.observer.event.DbTransactionOpenEvent;
import me.moonways.bridgenet.jdbc.core.observer.event.DbTransactionRollbackEvent;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.jdbc.provider.BridgenetJdbcProvider;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

public class OpenJdbcConnectionHook extends BootstrapHook {

    @Inject
    private BeansService beansService;
    @Inject
    private ResourcesAssembly assembly;
    @Inject
    private DatabaseProvider databaseProvider;
    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        if (databaseProvider != null) {
            BridgenetJdbcProvider.JdbcSettingsConfig jdbcSettingsConfig = readSettings();

            BridgenetJdbcProvider bridgenetJdbcProvider = new BridgenetJdbcProvider(databaseProvider);
            bridgenetJdbcProvider.initConnection(jdbcSettingsConfig);

            DatabaseConnection databaseConnection = bridgenetJdbcProvider.getDatabaseConnection();
            // todo fix connect not only for h2
            //databaseConnection.call("SET MODE MySQL;");

            handleDatabaseEventsForMetric(databaseConnection);

            bindAllBeans(bridgenetJdbcProvider);
        }
    }

    private void bindAllBeans(BridgenetJdbcProvider bridgenetJdbcProvider) {
        DatabaseProvider databaseProvider = bridgenetJdbcProvider.getDatabaseProvider();
        DatabaseConnection databaseConnection = bridgenetJdbcProvider.getDatabaseConnection();
        DatabaseComposer composer = databaseProvider.getComposer();

        beansService.bind(composer);
        beansService.bind(databaseConnection);
        beansService.bind(new EntityRepositoryFactory(composer, databaseConnection));
    }

    private BridgenetJdbcProvider.JdbcSettingsConfig readSettings() {
        return assembly.readJsonAtEntity(ResourcesTypes.JDBC_JSON,
                StandardCharsets.UTF_8,
                BridgenetJdbcProvider.JdbcSettingsConfig.class);
    }

    private void handleDatabaseEventsForMetric(DatabaseConnection connection) {
        connection.addObserver(new ObserverAdapter() {

            @Override
            protected void observe(DbRequestCompletedEvent event) {
                bridgenetDataLogger.logJdbcQueryCompleted();
            }

            @Override
            protected void observe(DbRequestFailureEvent event) {
                bridgenetDataLogger.logJdbcQueryFailed();
            }

            @Override
            protected void observe(DbTransactionOpenEvent event) {
                bridgenetDataLogger.logTransactionOpen();
            }

            @Override
            protected void observe(DbTransactionRollbackEvent event) {
                bridgenetDataLogger.logTransactionRollback();
            }
        });
    }
}
