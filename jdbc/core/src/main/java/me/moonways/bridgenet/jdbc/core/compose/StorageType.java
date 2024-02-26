package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum StorageType {

    VIEW("VIEW"),
    STORAGE("SCHEMA"),
    CONTAINER("TABLE"),
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
