package me.moonways.bridgenet.jdbc.core.observer;

import me.moonways.bridgenet.jdbc.core.ConnectionID;

public interface Observable {

    long getEventId();

    ConnectionID getConnectionID();
}
