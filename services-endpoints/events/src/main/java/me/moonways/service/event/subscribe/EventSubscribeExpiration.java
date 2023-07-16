package me.moonways.service.event.subscribe;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.moonways.service.api.events.exception.EventException;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public final class EventSubscribeExpiration {

    private final long startedTimeMillis;

    private final long timeout;
    private final TimeUnit timeUnit;

    private void validateUnit() {
        if (timeUnit == null) {
            throw new EventException("expiration time unit is null");
        }
    }

    public long toMillisecondsTimeout() {
        validateUnit();
        return TimeUnit.MILLISECONDS.convert(timeout, timeUnit);
    }

    public boolean isTimeoutExpired() {
        validateUnit();
        return startedTimeMillis + toMillisecondsTimeout() - System.currentTimeMillis() <= 0;
    }
}
