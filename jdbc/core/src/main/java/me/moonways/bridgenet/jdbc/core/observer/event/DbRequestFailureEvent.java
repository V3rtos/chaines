package me.moonways.bridgenet.jdbc.core.observer.event;

import lombok.Getter;
import lombok.val;
import me.moonways.bridgenet.jdbc.core.ConnectionID;

@Getter
public class DbRequestFailureEvent extends DbRequestPreprocessEvent {

    private static final String TO_STRING_FORMAT = "DbRequestFailureEvent(eventId=%s, connection=\"%s\", sql=\"%s\")";

    public DbRequestFailureEvent(long eventId, ConnectionID connectionID, String sql) {
        super(eventId, connectionID, sql);
    }

    @Override
    public String toString() {
        val eventId = getEventId();
        val connectionID = getConnectionID().getUniqueId();
        val sql = getSql();

        return String.format(TO_STRING_FORMAT, eventId, connectionID, sql);
    }
}
