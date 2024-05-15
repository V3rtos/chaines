package me.moonways.bridgenet.jdbc.core;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.jdbc.core.util.result.Result;
import me.moonways.bridgenet.jdbc.core.wrap.JdbcWrapper;
import me.moonways.bridgenet.jdbc.core.wrap.ResponseProvider;
import me.moonways.bridgenet.jdbc.core.wrap.ResultWrapper;
import me.moonways.bridgenet.jdbc.core.observer.DatabaseObserver;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;
import java.util.stream.Stream;

@Log4j2
@Builder
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class DatabaseConnection {

    private static final String[] FETCH_FUNCTIONS = {"SELECT", "SHOW", "DESCRIBE"};

    @Getter
    private final ConnectionID id;
    private final JdbcWrapper jdbcWrapper;

    public DatabaseConnection copyWithExceptionHandler(Thread.UncaughtExceptionHandler exceptionHandler) {
        return new DatabaseConnection(id, JdbcWrapper.builder()
                .exceptionHandler(exceptionHandler)
                .connectionID(jdbcWrapper.getConnectionID())
                .jdbc(jdbcWrapper.getJdbc())
                .observers(jdbcWrapper.getObservers())
                .currentlyWorker(jdbcWrapper.isCurrentlyWorker())
                .credentials(jdbcWrapper.getCredentials())
                .build());
    }

    public Result<ResponseStream> call(String sql) {
        if (!jdbcWrapper.isConnected()) {
            jdbcWrapper.reconnect();
        }

        Result<ResultWrapper> result = canUseFetch(sql)
                ? jdbcWrapper.executeFetch(sql) : jdbcWrapper.executeUpdate(sql);

        return result.map(this::toResponseImpl);
    }

    private boolean canUseFetch(String sql) {
        final String preparedSql = sql.trim().toUpperCase();
        return Stream.of(FETCH_FUNCTIONS)
                .anyMatch(preparedSql::startsWith);
    }

    @SneakyThrows
    private ResponseStream toResponseImpl(ResultWrapper result) {
        ResponseProvider responseProvider = new ResponseProvider(result);
        return responseProvider.getHandle();
    }

    public DatabaseConnection addObserver(@NotNull DatabaseObserver observer) {
        jdbcWrapper.addObserver(observer);
        return this;
    }

    public void openTransaction(TransactionIsolation isolation) {
        if (!jdbcWrapper.isConnected()) {
            jdbcWrapper.reconnect();
        }

        jdbcWrapper.setTransactionState(TransactionState.ACTIVE);
        jdbcWrapper.setTransactionIsolation(isolation);
    }

    public void openTransaction() {
        openTransaction(TransactionIsolation.SERIALIZABLE);
    }

    public void closeTransaction() {
        if (jdbcWrapper.isConnected()) {
            jdbcWrapper.setTransactionState(TransactionState.INACTIVE);
        }
    }

    public synchronized <T> T ofTransactionalGet(TransactionIsolation isolation, Supplier<T> transactionSupplier) {
        openTransaction(isolation);
        T value = transactionSupplier.get();
        closeTransaction();
        return value;
    }

    public synchronized <T> T ofTransactionalGet(Supplier<T> transactionSupplier) {
        return ofTransactionalGet(TransactionIsolation.SERIALIZABLE, transactionSupplier);
    }

    public synchronized void close() {
        jdbcWrapper.close();
    }
}
