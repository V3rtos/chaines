package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

@RequiredArgsConstructor
public enum ParameterType {

    INT("INT", new Class[]{int.class, Integer.class}, false, 0),
    BIGINT("BIGINT", new Class[]{long.class, Long.class}, false, 0),
    DOUBLE("REAL", new Class[]{double.class, Double.class}, false, 0),
    FLOAT("FLOAT", new Class[]{float.class, Float.class}, false, 0),
    SHORT("SMALLINT", new Class[]{short.class, Short.class}, false, 0),
    DECIMAL("DECIMAL", new Class[]{Number.class}, true, 10), // Длина по умолчанию 10

    STRING("VARCHAR", new Class[]{String.class}, true, 255), // Длина по умолчанию 255

    TIMESTAMP("TIMESTAMP", new Class[]{Timestamp.class}, false, 0),
    DATETIME("DATETIME", new Class[]{Date.class}, false, 0),
    TIME("TIME", new Class[]{Time.class}, false, 0),

    SERIALIZATION("BLOB", new Class[]{byte[].class, short[].class}, false, 0);

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
    private final boolean requiresLength; // Требуется ли длина
    private final int defaultLength;     // Длина по умолчанию

    public boolean isLengthRequired() {
        return requiresLength;
    }

    public int getDefaultLength() {
        return defaultLength;
    }

    @Override
    public String toString() {
        return toSqlString;
    }
}
