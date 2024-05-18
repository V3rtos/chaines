package me.moonways.bridgenet.assembly.ini.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class IniProperty {

    @Getter
    private final String key;

    private final String value;

    public String getAsString() {
        return value;
    }

    public boolean getAsBoolean() {
        return Boolean.parseBoolean(value);
    }

    public int getAsInt() {
        return Integer.parseInt(value);
    }

    public long getAsLong() {
        return Long.parseLong(value);
    }
}
