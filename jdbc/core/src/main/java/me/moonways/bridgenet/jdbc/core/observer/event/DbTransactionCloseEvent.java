package me.moonways.bridgenet.jdbc.core.observer.event;

import lombok.val;
import me.moonways.bridgenet.jdbc.core.ConnectionID;
import me.moonways.bridgenet.jdbc.core.observer.AbstractObservable;

public class DbTransactionCloseEvent extends AbstractObservable {

    private static final String TO_STRING_FORMAT = "DbTransactionCloseEvent(eventId=%s, connection=\"%s\")";

    public DbTransactionCloseEvent(long eventId, ConnectionID connectionID) {
        super(eventId, connectionID);
    }

    @Override
    public String toString() {
        val eventId = getEventId();
        val connectionID = getConnectionID().getUniqueId();

        return String.format(TO_STRING_FORMAT, eventId, connectionID);
    }
}
