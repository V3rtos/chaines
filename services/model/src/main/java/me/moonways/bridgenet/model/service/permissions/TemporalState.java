package me.moonways.bridgenet.model.service.permissions;

import lombok.*;

import java.time.Duration;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporalState {

    public static TemporalState enabled(Duration duration) {
        return new TemporalState(true, duration.toMillis());
    }

    public static TemporalState infinity() {
        return new TemporalState(false, 0);
    }

    private final boolean enabled;

    private final long totalCooldown;

    public Long withActualTimeMillis() {
        return isEnabled() ? System.currentTimeMillis() + totalCooldown : 0;
    }
}
