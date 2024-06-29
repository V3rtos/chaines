package me.moonways.bridgenet.jdbc.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.sql.Connection;

@Getter
@RequiredArgsConstructor
public enum TransactionIsolation {

    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private static final TransactionIsolation DEFAULT
            = TransactionIsolation.REPEATABLE_READ;

    public static TransactionIsolation getDefault() {
        return DEFAULT;
    }

    private final int level;
}
