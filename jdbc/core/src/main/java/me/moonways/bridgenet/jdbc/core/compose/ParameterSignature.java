package me.moonways.bridgenet.jdbc.core.compose;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.ApiStatus;

@RequiredArgsConstructor
public enum ParameterSignature {

    NOTNULL("NOT NULL"),
    AUTO_GENERATED("AUTO_INCREMENT"),
    UNIQUE("UNIQUE"),
    @ApiStatus.Internal
    @Deprecated
    KEY(""),
    PRIMARY(""), // moved to other collection "primary" and "has_primary"
    ;

    private final String toSqlString;

    @Override
    public String toString() {
        return toSqlString;
    }
}
