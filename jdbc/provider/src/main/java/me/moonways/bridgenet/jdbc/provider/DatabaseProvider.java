package me.moonways.bridgenet.jdbc.provider;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.core.ConnectionID;
import me.moonways.bridgenet.jdbc.core.DatabaseConnection;
import me.moonways.bridgenet.jdbc.core.compose.DatabaseComposer;
import me.moonways.bridgenet.jdbc.core.compose.impl.PatternDatabaseComposerImpl;
import me.moonways.bridgenet.jdbc.core.security.Credentials;
import me.moonways.bridgenet.jdbc.core.transaction.PreparedTransaction;
import me.moonways.bridgenet.jdbc.core.transaction.Transaction;
import me.moonways.bridgenet.jdbc.core.transaction.TransactionFactory;
import me.moonways.bridgenet.jdbc.core.transaction.TransactionQuery;
import me.moonways.bridgenet.jdbc.core.transaction.impl.DatabaseTransactionQuery;
import me.moonways.bridgenet.jdbc.core.transaction.repository.TransactionRepository;
import me.moonways.bridgenet.jdbc.core.wrap.JdbcWrapper;
import me.moonways.bridgenet.jdbc.dao.EntityDao;
import me.moonways.bridgenet.jdbc.dao.TypedEntityDao;
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

    private final TransactionFactory transactionFactory = new TransactionFactory();

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

    public PreparedTransaction prepareTransaction(TransactionRepository repository) {
        return transactionFactory.createPreparedTransaction(repository);
    }

    public synchronized Transaction openTransaction() {
        return transactionFactory.createTransaction();
    }

    public synchronized TransactionQuery openTransactionQuery() {
        return new DatabaseTransactionQuery();
    }

    public synchronized Collection<DatabaseConnection> getActiveConnections() {
        return Collections.unmodifiableCollection(activeConnections);
    }

    public <E> EntityDao<E> createDao(@NotNull Class<E> entity, @NotNull DatabaseConnection connection) {
        return new TypedEntityDao<>(composer, connection, entity);
    }
}
