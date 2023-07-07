package me.moonways.bridgenet.service.game.type.old;

import org.jetbrains.annotations.NotNull;

public interface ServerModeType extends ServerType {

    int getTypeId();

    String getTypeName();

    @NotNull
    ServerType getServerType();
}
