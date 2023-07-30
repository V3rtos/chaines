package me.moonways.model.games.type.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum SurvivalType implements ServerType {

    CLASSIC(100, "Classic"),
    ONE_BLOCK(200, "OneBlock"),
    SKY_BLOCK(300, "SkyBlock"),
    WINTER_ADVENTURES(400, "WinterAdventures"),
    FANTASY_UNIVERSE(500, "FantasyUniverse"),
    ANARCHY(600, "Anarchy"),
    ;

    private final int typeId;

    private final String typeName;
}
