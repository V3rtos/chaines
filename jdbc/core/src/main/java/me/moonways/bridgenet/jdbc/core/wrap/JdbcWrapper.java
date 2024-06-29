package me.moonways.bridgenet.jdbc.core.wrap;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.util.pair.Pair;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.jdbc.core.ConnectionID;
import me.moonways.bridgenet.jdbc.core.TransactionIsolation;
import me.moonways.bridgenet.jdbc.core.TransactionState;
import me.moonways.bridgenet.jdbc.core.observer.DatabaseObserver;
import me.moonways.bridgenet.jdbc.core.observer.Observable;
import me.moonways.bridgenet.jdbc.core.observer.event.*;
import me.moonways.bridgenet.jdbc.core.security.Credentials;
import me.moonways.bridgenet.jdbc.core.util.SqlFunction;
import me.moonways.bridgenet.jdbc.core.util.result.Result;
import org.jetbrains.annotations.NotNull;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

@Getter
@Log4j2
@Builder
public class JdbcWrapper {
    private static final int VALID_TIMEOUT = 3500;

    @RequiredArgsConstructor
    private static class PreparedQuerySession {

        private final PreparedStatement statement;
        private final boolean useGeneratedKeys;
    }

    private final ExecutorService threadExecutor = Threads.newCachedThreadPool();

    private final ConnectionID connectionID;
    private final Credentials credentials;
    private final Thread.UncaughtExceptionHandler exceptionHandler;

    private Connection jdbc;

    private List<DatabaseObserver> observers;
    private boolean currentlyWorker;

    private int transactionMod;

    @SneakyThrows
    public boolean isConnected() {
        return jdbc != null && !jdbc.isClosed() && jdbc.isValid(VALID_TIMEOUT);
    }

    public synchronized void addObserver(@NotNull DatabaseObserver observer) {
        if (observers == null) {
            observers = new CopyOnWriteArrayList<>();
        }
        observers.add(observer);
    }

    private void observe(@NotNull Observable event) {
        threadExecutor.submit(() -> {
            if (observers != null) {
                observers.forEach(observer -> observer.observe(event));
            }
        });
    }

    public synchronized void connect() {
        try {
            if (jdbc == null || !jdbc.isValid(VALID_TIMEOUT)) {
                observe(new DbConnectEvent(System.currentTimeMillis(), connectionID));
                initConnection();
            }
        } catch (SQLException exception) {
            exceptionHandler.uncaughtException(Thread.currentThread(), exception);
        }
    }

    private void initConnection() throws SQLException {
        String passwordString = new String(credentials.getPassword());
        jdbc = DriverManager.getConnection(credentials.getUri(), credentials.getUsername(), passwordString);

        log.debug("Connection '{}' was initialized by {}", connectionID.getUniqueId(), credentials);
    }

    public void reconnect() {
        try {
            if (jdbc == null || jdbc.isClosed() || !jdbc.isValid(VALID_TIMEOUT)) {
                observe(new DbReconnectPreprocessEvent(System.currentTimeMillis(), connectionID));
                connect();
            }
        } catch (SQLException exception) {
            exceptionHandler.uncaughtException(Thread.currentThread(), exception);
        }
    }

    public synchronized void close() {
        try {
            if (jdbc != null && (jdbc.isClosed() || !jdbc.isValid(VALID_TIMEOUT))) {
                observe(new DbClosedEvent(System.currentTimeMillis(), connectionID));
                jdbc.close();

                log.debug("Connection was closed as force");
            }
        } catch (SQLException exception) {
            exceptionHandler.uncaughtException(Thread.currentThread(), exception);
        }
    }

    @SneakyThrows
    private Result<ResultWrapper> executeOrdered(String sql,
                                                 SqlFunction<PreparedQuerySession, ResultWrapper> resultLookup) {
        log.debug("Inbound Query: §2{}", sql);

        final Thread thread = Thread.currentThread();
        final Result<ResultWrapper> result = Result.ofEmpty();
        try {
            Pair<PreparedStatement, Boolean> statementAndGeneratedKeysPair = tryStatementPrepareWithGeneratedKeys(sql);

            PreparedStatement statement = statementAndGeneratedKeysPair.first();
            Boolean supportsGeneratedKeys = statementAndGeneratedKeysPair.second();

            observe(new DbRequestPreprocessEvent(System.currentTimeMillis(), connectionID, sql));
            result.beginIntent()
                    .completeIntent(() -> {
                try {
                    ResultWrapper resultWrapper = resultLookup.get(new PreparedQuerySession(statement, supportsGeneratedKeys));
                    observe(new DbRequestCompletedEvent(System.currentTimeMillis(), connectionID, sql, result));
                    return resultWrapper;
                } catch (SQLException exception) {
                    if (transactionMod != 0) {
                        try {
                            log.debug("Transaction session is rollback");
                            jdbc.rollback();
                        } catch (SQLException e) {
                            exception.addSuppressed(e);
                        }

                        flushTransactionsQueue();
                        setTransactionState(TransactionState.INACTIVE);
                    }

                    observe(new DbRequestFailureEvent(System.currentTimeMillis(), connectionID, sql));
                    exceptionHandler.uncaughtException(thread, exception);
                    return null;
                }
            });

        } catch (SQLException exception) {
            observe(new DbRequestFailureEvent(System.currentTimeMillis(), connectionID, sql));
            exceptionHandler.uncaughtException(thread, exception);
        }
        return result;
    }

    public Result<ResultWrapper> executeUpdate(String sql) {
        return executeOrdered(sql, (session) -> {
            long affectedRows = isLargeUpdate(sql)
                    ? session.statement.executeLargeUpdate()
                    : session.statement.executeUpdate();

            ResultSet generatedKeys = null;

            if (session.useGeneratedKeys) {
                generatedKeys = session.statement.getGeneratedKeys();
            }

            return ResultWrapper.builder()
                    .affectedRows(affectedRows)
                    .statement(session.statement)
                    .result(generatedKeys)
                    .build();
        });
    }

    public Result<ResultWrapper> executeFetch(String sql) {
        return executeOrdered(sql, (session) -> {
            final ResultSet resultSet = session.statement.executeQuery();

            return ResultWrapper.builder()
                    .affectedRows(resultSet.getFetchSize())
                    .statement(session.statement)
                    .result(resultSet)
                    .build();
        });
    }

    public void flushTransactionsQueue() {
        if (transactionMod != 0) {
            transactionMod = 1;
        }
    }

    @SuppressWarnings("MagicConstant")
    public void setTransactionIsolation(TransactionIsolation isolation) {
        try {
            if (transactionMod == 0) {
                jdbc.setTransactionIsolation(isolation.getLevel());
                log.debug("Transaction session isolation was changed to {}", isolation);
            }
        } catch (SQLException exception) {
            exceptionHandler.uncaughtException(Thread.currentThread(), exception);
        }
    }

    public void setTransactionState(TransactionState state) {
        Thread thread = Thread.currentThread();
        switch (state) {
            case ACTIVE: {
                try {
                    if (transactionMod <= 0) {
                        jdbc.setAutoCommit(false);
                        log.debug("Transaction session is opened");

                        observe(new DbTransactionOpenEvent(System.currentTimeMillis(), connectionID));
                        transactionMod = 0;
                    }
                    transactionMod++;
                } catch (SQLException exception) {
                    exceptionHandler.uncaughtException(thread, exception);
                }
                break;
            }
            case INACTIVE: {
                try {
                    if (transactionMod == 1) {
                        jdbc.commit();
                        jdbc.setAutoCommit(true);

                        log.debug("Transaction session is commited");

                        observe(new DbTransactionCloseEvent(System.currentTimeMillis(), connectionID));
                        transactionMod = 0;
                    } else {
                        transactionMod--;
                    }
                } catch (SQLException exception) {
                    log.debug("Transaction session is rollback");
                    try {
                        jdbc.rollback();
                        observe(new DbTransactionRollbackEvent(System.currentTimeMillis(), connectionID));
                    } catch (SQLException e) {
                        exception.addSuppressed(e);
                    }
                    exceptionHandler.uncaughtException(thread, exception);
                }
                break;
            }
        }
    }

    private Pair<PreparedStatement, Boolean> tryStatementPrepareWithGeneratedKeys(String sql) throws SQLException {
        try {
            return Pair.immutable(jdbc.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS), true);
        } catch (Exception ignored) {
            return Pair.immutable(jdbc.prepareStatement(sql, Statement.NO_GENERATED_KEYS), false);
        }
    }

    private boolean isLargeUpdate(String sql) {
        return sql != null && (sql.trim().toUpperCase().startsWith("UPDATE")
                || sql.trim().toUpperCase().startsWith("DELETE"));
    }
}
