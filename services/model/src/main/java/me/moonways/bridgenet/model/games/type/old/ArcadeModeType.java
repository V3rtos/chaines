package me.moonways.bridgenet.model.games.type.old;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.jetbrains.annotations.NotNull;

@Getter
@ToString
@RequiredArgsConstructor
public enum ArcadeModeType implements ServerModeType {

    HIDE_AND_SEEK(1, "HideAndSeek"),
    SPEED_BUILDERS(2, "SpeedBuilders"),
    TNT_RUN(3, "TNTRun"),
    TNT_TAG(4, "TNTTag"),
    ;

    private final int typeId;

    private final String typeName;

    @NotNull
    @Override
    public ServerType getServerType() {
        return GameType.ARCADES;
    }
}
