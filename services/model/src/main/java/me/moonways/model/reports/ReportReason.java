package me.moonways.model.reports;

import java.io.Serializable;

public enum ReportReason implements Serializable {

    CHAT_INSULTS,
    CHAT_SABOTAGE,
    CHAT_PROVOCATION,
    TEAM_PROVOCATION,
    TEAM_SABOTAGE,
    CALL_FOR_ILLEGAL_ACTIONS,
    CHEATING,
    OTHER,
}
