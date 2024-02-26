package me.moonways.bridgenet.jdbc.core.observer.event;

import lombok.Getter;
import lombok.val;
import me.moonways.bridgenet.jdbc.core.ConnectionID;
import me.moonways.bridgenet.jdbc.core.util.result.Result;
import me.moonways.bridgenet.jdbc.core.wrap.ResultWrapper;

@Getter
public class DbRequestCompletedEvent extends DbRequestPreprocessEvent {

    private static final String TO_STRING_FORMAT = "DbRequestCompletedEvent(eventId=%s, connection=\"%s\", sql=\"%s\")";

    private final Result<ResultWrapper> result;

    public DbRequestCompletedEvent(long eventId, ConnectionID connectionID, String sql, Result<ResultWrapper> result) {
        super(eventId, connectionID, sql);

        this.result = result;
    }

    @Override
    public String toString() {
        val eventId = getEventId();
        val connectionID = getConnectionID().getUniqueId();
        val sql = getSql();

        return String.format(TO_STRING_FORMAT, eventId, connectionID, sql);
    }
}
