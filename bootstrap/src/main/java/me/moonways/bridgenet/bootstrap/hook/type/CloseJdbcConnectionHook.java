package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.provider.DatabaseProvider;
import org.jetbrains.annotations.NotNull;

public class CloseJdbcConnectionHook extends ApplicationBootstrapHook {

    @Inject
    private DatabaseProvider databaseProvider;
    @Inject
    private DatabaseConnection databaseConnection;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        databaseProvider.closeConnection(databaseConnection);
    }
}
