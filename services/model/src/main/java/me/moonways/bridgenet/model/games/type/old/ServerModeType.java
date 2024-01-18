package me.moonways.bridgenet.model.games.type.old;

import org.jetbrains.annotations.NotNull;

public interface ServerModeType extends ServerType {

    int getTypeId();

    String getTypeName();

    @NotNull
    ServerType getServerType();
}
