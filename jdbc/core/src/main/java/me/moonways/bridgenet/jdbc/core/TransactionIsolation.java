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

    public static TransactionIsolation getDefault() {
        return TransactionIsolation.REPEATABLE_READ;
    }

    private final int level;
}
