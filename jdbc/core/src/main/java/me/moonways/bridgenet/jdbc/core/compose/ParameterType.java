package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@RequiredArgsConstructor
public enum ParameterType {

    INT("INT", new Class[]{int.class, Integer.class}),
    BIGINT("BIGINT", new Class[]{long.class, Long.class}),
    DOUBLE("REAL", new Class[]{double.class, Double.class}),
    FLOAT("FLOAT", new Class[]{float.class, Float.class}),
    SHORT("SMALLINT", new Class[]{short.class, Short.class}),
    DECIMAL("DECIMAL", new Class[]{Number.class}),

    STRING("VARCHAR", new Class[]{String.class}),

    TIMESTAMP("TIMESTAMP", new Class[]{Timestamp.class}),
    DATETIME("DATETIME", new Class[]{Date.class}),
    TIME("TIME", new Class[]{Time.class}),

    SERIALIZATION("BLOB", new Class[]{byte[].class, short[].class});

    private static final ParameterType[] TYPES = values();

    public static ParameterType fromJavaType(Class<?> cls) {
        if (cls == null) {
            return SERIALIZATION;
        }
        for (ParameterType parameterType : TYPES) {
            for (Class<?> javaType : parameterType.javaTypes) {

                if (javaType.equals(cls)) {
                    return parameterType;
                }
            }
        }

        return null;
    }

    private final String toSqlString;
    private final Class<?>[] javaTypes;

    @Override
    public String toString() {
        return toSqlString;
    }
}
