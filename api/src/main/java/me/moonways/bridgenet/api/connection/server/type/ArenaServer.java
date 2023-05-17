package me.moonways.bridgenet.api.connection.server.type;

import lombok.Getter;
import me.moonways.bridgenet.api.connection.player.ConnectedPlayer;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class ArenaServer extends SpigotServer {

    private final UUID arenaUUID;

    private final String mapName;

    private final int gameId;
    private final int modeId;

    private final int maxPlayers;

    public ArenaServer(@NotNull UUID arenaUUID,
                       @NotNull String mapName,
                       int gameId,
                       int modeId,
                       int maxPlayers,
                       @NotNull String name,
                       @NotNull BridgenetChannel bridgenetChannel,
                       @NotNull InetSocketAddress serverAddress) {
        super(name, bridgenetChannel, serverAddress);

        this.arenaUUID = arenaUUID;
        this.mapName = mapName;
        this.gameId = gameId;
        this.modeId = modeId;
        this.maxPlayers = maxPlayers;
    }

    @Override
    public CompletableFuture<Boolean> connect(@NotNull ConnectedPlayer player) {
        // TODO: 17.05.2023 throw custom packet
        return null;
    }
}
