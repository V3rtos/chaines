package me.moonways.model.games.type.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@RequiredArgsConstructor
public enum DuelsModeType implements ServerModeType {

    CLASSIC(1, "Classic"),
    GAPPLE(2, "GApple"),
    BOXING(3, "Boxing"),
    SUMO(4, "Sumo"),
    THE_BRIDGE(5, "TheBridge"),
    ;

    private final int typeId;

    private final String typeName;

    @NotNull
    @Override
    public ServerType getServerType() {
        return GameType.DUELS;
    }
}
