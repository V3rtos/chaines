package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

@RequiredArgsConstructor
public enum ParameterAddon {

    NOTNULL("NOT NULL"),
    INCREMENTING("AUTO_INCREMENT"),
    UNIQUE("UNIQUE"),
    @ApiStatus.Internal
    KEY(""),
    PRIMARY(""), // moved to other collection "primary" and "has_primary"
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
