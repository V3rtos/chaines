package me.moonways.bridgenet.assembly.ini;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.assembly.ini.type.IniGroup;
import me.moonways.bridgenet.assembly.ini.type.IniProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class IniConfig {

    private final Set<IniGroup> groups;

    public Optional<IniGroup> readGroup(String groupName) {
        return groups.stream()
                .filter(group -> group.getKey().equals(groupName))
                .findFirst();
    }

    public Optional<IniProperty> readProperty(String group, String field) {
        final Optional<IniGroup> IniGroup = readGroup(group);
        return IniGroup
                .flatMap(value -> Stream.of(value.getProperties())
                        .filter(property -> property.getKey().equals(field))
                        .findFirst());
    }

    public String readString(@NotNull String group, @NotNull String field, @Nullable String def) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsString).orElse(def);
    }

    public Optional<String> readString(@NotNull String group, @NotNull String field) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsString);
    }

    public boolean readBoolean(@NotNull String group, @NotNull String field, boolean def) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsBoolean).orElse(def);
    }

    public Optional<Boolean> readBoolean(@NotNull String group, @NotNull String field) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsBoolean);
    }

    public int readInt(@NotNull String group, @NotNull String field, int def) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsInt).orElse(def);
    }

    public Optional<Integer> readInt(@NotNull String group, @NotNull String field) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsInt);
    }

    public long readLong(@NotNull String group, @NotNull String field, long def) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsLong).orElse(def);
    }

    public Optional<Long> readLong(@NotNull String group, @NotNull String field) {
        final Optional<IniProperty> iniProperty = readProperty(group, field);
        return iniProperty.map(IniProperty::getAsLong);
    }
}
