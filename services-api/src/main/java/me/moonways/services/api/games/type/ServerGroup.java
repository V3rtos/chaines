package me.moonways.services.api.games.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum ServerGroup {

    SKY_WARS(10, ServerCriteria.GAME,
            "SkyWars",
            "minecraft:ender_pearl",
            new ServerMode[]{
                    ServerMode.RANKED,
                    ServerMode.SOLO,
                    ServerMode.DOUBLE,
                    ServerMode.TRIPLE,
                    ServerMode.TEAM,
            }),
    BED_WARS(20, ServerCriteria.GAME,
            "BedWars", "minecraft:red_bed",
            new ServerMode[]{
                    ServerMode.RANKED,
                    ServerMode.SOLO,
                    ServerMode.DOUBLE,
                    ServerMode.TRIPLE,
                    ServerMode.TEAM,
            }),
    BUILD_BATTLE(30, ServerCriteria.GAME,
            "BuildBattle", "minecraft:workbench",
            new ServerMode[]{
                    ServerMode.SOLO,
                    ServerMode.DOUBLE,
            }),
    DUELS(40, ServerCriteria.GAME,
            "Duels", "minecraft:iron_sword",
            ServerMode.UNDEFINED_MODE_ARRAY),
    ARCADES(50, ServerCriteria.GAME,
            "Arcades", "minecraft:bowl",
            ServerMode.UNDEFINED_MODE_ARRAY),

    UNDEFINED(-1, ServerCriteria.GENERAL,
            "Undefined", "minecraft:barrier",
            ServerMode.UNDEFINED_MODE_ARRAY),
    ;

    private final int id;

    private final ServerCriteria criteria;

    private final String name;
    private final String minecraftIconName;

    private final ServerMode[] actualModesArray;
}
