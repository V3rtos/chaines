package me.moonways.bridgenet.system.connection;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.connection.server.AddressServerMap;
import me.moonways.bridgenet.api.connection.server.ServerManager;
import me.moonways.bridgenet.api.connection.server.type.ArenaServer;
import me.moonways.bridgenet.api.connection.server.type.GameServer;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.ServerConnectResponseType;
import me.moonways.bridgenet.services.connection.server.message.ArenaRegisterRequestMessage;
import me.moonways.bridgenet.services.connection.server.message.ArenaRegisterResponseMessage;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;
import java.util.UUID;

@MessageHandler
@Log4j2
public class ArenaChannelHandler {

    @Inject
    @Getter
    private ServerManager serverManager;

    @MessageTrigger
    public void handle(ArenaRegisterRequestMessage message) {
        AddressServerMap addressServerMap = serverManager.getAddressServerMap();

        int clientPort = message.getChannel().getInetSocketAddress().getPort();

        GameServer gameServer = addressServerMap.getUncheckedServer(clientPort);

        if (gameServer == null) {
            writeArenaRegisterResponse(message, ServerConnectResponseType.SERVER_NOT_FOUND);
            return;
        }

        UUID arenaUUID = message.getArenaUUID();

        String mapName = message.getMapName();

        int gameId = message.getGameId();
        int modeId = message.getModeId();

        int maxPlayers = message.getMaxPlayers();

        ArenaServer arenaServer = createArenaServer(arenaUUID,
                mapName,
                gameId,
                modeId,
                maxPlayers,
                gameServer.getName(),
                gameServer.getBridgenetChannel(),
                gameServer.getServerAddress());


        boolean registered = gameServer.addArena(arenaUUID, arenaServer);

        if (registered) {
            writeArenaRegisterResponse(message, ServerConnectResponseType.ALREADY_CONNECTED);
            return;
        }

        writeArenaRegisterResponse(message, ServerConnectResponseType.SUCCESSFUL_CONNECTED);

        log.info("Arena {} by game {} successful registered", arenaUUID, gameServer.getName());
    }


    private void writeArenaRegisterResponse(@NotNull Message message, @NotNull ServerConnectResponseType serverConnectResponseType) {
        int arenaConnectResponseTypeId = serverConnectResponseType.getIdentifier();

        message.writeResponse(new ArenaRegisterResponseMessage(arenaConnectResponseTypeId));
    }

    private ArenaServer createArenaServer(@NotNull UUID arenaUUID,
                                          @NotNull String mapName,
                                          int gameId,
                                          int modeId,
                                          int maxPlayers,
                                          @NotNull String name,
                                          @NotNull BridgenetChannel channel,
                                          @NotNull InetSocketAddress inetSocketAddress) {
        ArenaServer arenaServer = new ArenaServer(
                arenaUUID,
                mapName,
                gameId,
                modeId,
                maxPlayers,
                name,
                channel,
                inetSocketAddress);

        log.info("Arena {} by game {} successful created", arenaUUID, name);

        return arenaServer;
    }
}
