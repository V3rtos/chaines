package me.moonways.model.players.permission;

import lombok.*;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class TemporalState {

    public static TemporalState createEnabled(int cooldownDelay, TimeUnit unit) {
        return new TemporalState(true, TimeUnit.MILLISECONDS.convert(cooldownDelay, unit));
    }

    public static TemporalState createDisabled() {
        return new TemporalState(false, 0);
    }

    private final boolean enabled;
    private final long totalCooldown;
}
