package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ConditionMatcher {

    EQUALS("="),
    MORE(">"),
    LESS("<"),
    MORE_OR_EQUAL(">="),
    LESS_OR_EQUAL("<="),
    LIKE_IS("LIKE"),
    IS("IS"),
    INSIDE("IN"),
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
