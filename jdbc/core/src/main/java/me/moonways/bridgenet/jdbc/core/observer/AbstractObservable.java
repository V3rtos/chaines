package me.moonways.bridgenet.jdbc.core.observer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.jdbc.core.ConnectionID;

@Getter
@RequiredArgsConstructor
public class AbstractObservable implements Observable {

    private final long eventId;

    private final ConnectionID connectionID;
}
