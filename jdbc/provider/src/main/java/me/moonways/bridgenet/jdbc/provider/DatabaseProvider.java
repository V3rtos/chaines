package me.moonways.bridgenet.jdbc.provider;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.core.ConnectionID;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.core.compose.impl.PatternDatabaseComposerImpl;
import me.moonways.bridgenet.jdbc.core.security.Credentials;
import me.moonways.bridgenet.jdbc.core.wrap.JdbcWrapper;
import org.jetbrains.annotations.NotNull;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArraySet;

@Log4j2
public final class DatabaseProvider {

    private final Set<DatabaseConnection> activeConnections = new CopyOnWriteArraySet<>();

    @Getter
    private final DatabaseComposer composer = new PatternDatabaseComposerImpl();

    private DatabaseConnection createDatabaseConnection(Credentials credentials) {
        ConnectionID connectionID = ConnectionID.builder()
                .setUniqueId(UUID.randomUUID())
                .setOpenedTimestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        return DatabaseConnection.builder()
                .id(connectionID)
                .jdbcWrapper(JdbcWrapper.builder()
                        .connectionID(connectionID)
                        .exceptionHandler((t, e) -> log.error("§4Bridgenet database-framework thread '{}' caught an exception:", t.getName(), e))
                        .credentials(credentials)
                        .build())
                .build();
    }

    public synchronized DatabaseConnection openConnection(@NotNull Credentials credentials) {
        DatabaseConnection databaseConnection = createDatabaseConnection(credentials);
        activeConnections.add(databaseConnection);

        return databaseConnection;
    }

    public synchronized void closeConnection(@NotNull DatabaseConnection connection) {
        activeConnections.remove(connection);
        connection.close();
    }

    public synchronized Collection<DatabaseConnection> getActiveConnections() {
        return Collections.unmodifiableCollection(activeConnections);
    }
}
