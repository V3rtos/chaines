package me.moonways.bridgenet.scheduler;

import lombok.*;

import java.util.concurrent.TimeUnit;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ScheduledTime {

    public static ScheduledTime zero() {
        return new ScheduledTime(0, TimeUnit.MILLISECONDS);
    }

    public static ScheduledTime of(long delay, TimeUnit unit) {
        return new ScheduledTime(delay, unit);
    }

    public static ScheduledTime ofMillis(long delay) {
        return of(delay, TimeUnit.MILLISECONDS);
    }

    public static ScheduledTime ofSeconds(long delay) {
        return of(delay, TimeUnit.SECONDS);
    }

    private final long delay;
    private final TimeUnit unit;

    public long toNanos() {
        return TimeUnit.NANOSECONDS.convert(delay, unit);
    }

    public long toMillis() {
        return TimeUnit.MILLISECONDS.convert(delay, unit);
    }

    public boolean isZero() {
        return delay <= 0;
    }
}
