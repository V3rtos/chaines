package me.moonways.bridgenet.model.service.permissions.permission;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.model.service.permissions.TemporalState;
import org.jetbrains.annotations.NotNull;

import java.time.Duration;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class Permission {

    public static Permission named(@NotNull String name) {
        return new Permission(TemporalState.infinity(), name);
    }

    public static Permission temp(@NotNull String name, @NotNull Duration duration) {
        return new Permission(TemporalState.enabled(duration), name);
    }

    private final TemporalState temporalState;

    @EqualsAndHashCode.Include
    private final String name;
}
