package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum AggregateFunc {

    COUNTING("COUNT"),
    AVERAGE("AVG"),
    MINIMAL("MIN"),
    MAXIMAL("MAX"),
    SUMMING("SUM"),
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
