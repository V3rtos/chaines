package me.moonways.model.games.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public enum GameSubmode {

    SOLO("Solo", "1vs1", 1),
    DOUBLES("Doubles", "2vs2", 2),
    TRIPLES("Triples", "3vs3", 3),
    PARTY("Party", "4vs4", 4),
    MEGA_PARTY("MegaParty", "5vs5", 5);

    private final String displayName;
    private final String rawName;

    private final int teamSize;
}
