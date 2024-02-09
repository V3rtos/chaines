package me.moonways.bridgenet.endpoint.servers.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.endpoint.servers.ConnectedServerStub;
import me.moonways.bridgenet.endpoint.servers.ServersContainer;
import me.moonways.bridgenet.endpoint.servers.players.PlayersOnServersConnectionService;
import me.moonways.bridgenet.model.bus.message.Disconnect;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.bus.message.Redirect;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServerFlag;
import me.moonways.bridgenet.model.servers.ServerInfo;
import me.moonways.bridgenet.model.servers.ServersServiceModel;
import me.moonways.bridgenet.mtp.message.InputMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.IncomingMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@IncomingMessageListener
public class ServersInputMessagesListener {

    private final ServersContainer container;
    private final ServersServiceModel serversServiceModel;

    @Inject
    private BeansService beansService;

    @Inject
    private PlayersOnServersConnectionService playersOnServersConnectionService;

    @Inject
    private PlayersServiceModel playersServiceModel;

    @SubscribeMessage
    public void handleHandshake(InputMessageContext<Handshake> input) {
        Handshake handshake = input.getMessage();

        if (handshake.getType() == Handshake.Type.SERVER) {

            ServerInfo serverInfo = toServerInfo(handshake.getProperties());
            registerServer(input, serverInfo);
        }
    }

    @SubscribeMessage
    public void handleRedirection(InputMessageContext<Redirect> input) {
        Redirect redirect = input.getMessage();

        UUID playerUUID = redirect.getPlayerUUID();
        UUID serverKey = redirect.getServerKey();

        ConnectedServerStub server = container.getConnectedServerExact(serverKey);

        if (server == null) {
            log.info("§4Server by key '{}' is not connected", serverKey);
            return;
        }

        UUID currentServerKey = playersOnServersConnectionService.getPlayerCurrentServerKey(playerUUID)
                .orElse(null);

        if (!serverKey.equals(currentServerKey)) {
            playersOnServersConnectionService.insert(playerUUID, serverKey);
            input.answer(new Redirect.Success(playerUUID, serverKey));
        } else {
            input.answer(new Redirect.Failure(playerUUID, serverKey));
        }
    }

    @SubscribeMessage
    public void handle(Disconnect disconnect) {
        if (disconnect.getType() == Disconnect.Type.SERVER) {
            container.unregisterServer(disconnect.getUuid());
        }
    }

    private void registerServer(InputMessageContext<Handshake> input, ServerInfo serverInfo) {
        UUID serverKey = container.getExactServerKey(serverInfo.getName());

        if (serverKey == null) {
            ConnectedServerStub server = createServer(input, serverInfo);
            serverKey = container.registerServer(server);

            doSuccessRegister(serverKey, server);

            log.info("Server §2{} §rwas registered now by key: §2{}", serverInfo.getName(), serverKey);
            input.answer(new Handshake.Success(serverKey));

        } else {
            log.info("§4Server {} has already registered by key: {}", serverInfo.getName(), serverKey);
            input.answer(new Handshake.Failure(serverKey));
        }
    }

    private void doSuccessRegister(UUID serverKey, ConnectedServerStub server) {
        server.setUniqueId(serverKey);
        server.getChannel().setProperty(EntityServer.CHANNEL_PROPERTY, server);
    }

    private ConnectedServerStub createServer(InputMessageContext<Handshake> input, ServerInfo serverInfo) {
        return ConnectedServerStub.builder()
                .serverInfo(serverInfo)
                .channel(input.getChannel())
                .build();
    }

    private List<ServerFlag> getServerFlags(Properties properties) {
        List<ServerFlag> resultList = new ArrayList<>();

        if (properties.contains("server.flags.fallback"))
            resultList.add(ServerFlag.FALLBACK_SERVER);
        if (properties.contains("server.flags.default"))
            resultList.add(ServerFlag.DEFAULT_SERVER);
        if (properties.contains("server.flags.lobby"))
            resultList.add(ServerFlag.LOBBY_SERVER);

        return resultList;
    }

    private ServerInfo toServerInfo(Properties properties) {
        InetSocketAddress address = new InetSocketAddress(
                properties.getProperty("server.address.host"),
                Integer.parseInt(properties.getProperty("server.address.port")));

        return ServerInfo.builder()
                .address(address)
                .name(properties.getProperty("server.name"))
                .flags(getServerFlags(properties))
                .build();
    }
}
