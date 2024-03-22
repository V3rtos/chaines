package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParameterAddon {

    NOTNULL("NOT NULL"),
    INCREMENTING("AUTO_INCREMENT"),
    UNIQUE("UNIQUE"),
    PRIMARY(""), // moved to other collection "primary" and "has_primary"
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
