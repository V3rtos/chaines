package me.moonways.bridgenet.jdbc.core.util;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlFunction<T, R> {
    R get(T obj) throws SQLException;
}