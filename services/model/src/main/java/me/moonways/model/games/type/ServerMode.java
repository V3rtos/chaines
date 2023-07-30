package me.moonways.model.games.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum ServerMode {

    SOLO("s"),
    DOUBLE("d"),
    TRIPLE("tr"),
    TEAM("t"),
    RANKED("r"),
    UNDEFINED(""),
    ;

    public static final ServerMode[] UNDEFINED_MODE_ARRAY = new ServerMode[]{
            ServerMode.UNDEFINED
    };

    private final String serverNameSuffix;
}
