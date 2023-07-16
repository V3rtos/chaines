package me.moonways.service.api.games.type.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum GameType implements ServerType {

    SKY_WARS(10, "SkyWars"),
    BED_WARS(20, "BedWars"),
    DUELS(30, "Duels"),
    ARCADES(40, "Arcades"),
    BUILD_BATTLE(50, "BuildBattle");

    private final int typeId;

    private final String typeName;
}
