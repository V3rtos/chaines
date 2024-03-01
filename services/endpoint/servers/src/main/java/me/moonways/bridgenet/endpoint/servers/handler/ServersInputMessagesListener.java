package me.moonways.bridgenet.endpoint.servers.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventService;
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
import me.moonways.bridgenet.model.servers.event.ServerDisconnectEvent;
import me.moonways.bridgenet.model.servers.event.ServerHandshakeEvent;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
@InboundMessageListener
public class ServersInputMessagesListener {

    private final ServersContainer container;

    @Inject
    private BeansService beansService;
    @Inject
    private EventService eventService;
    @Inject
    private PlayersOnServersConnectionService playersOnServersConnectionService;
    @Inject
    private PlayersServiceModel playersServiceModel;

    private void callServerDisconnectEvent(EntityServer entityServer) {
        ServerDisconnectEvent event = new ServerDisconnectEvent(ServerDisconnectEvent.DownstreamType.DISCONNECT_MESSAGE, entityServer);
        eventService.fireEvent(event);
    }

    private void callServerHandshakeEvent(Handshake handshake, EntityServer entityServer) {
        ServerHandshakeEvent event = new ServerHandshakeEvent(handshake, entityServer);
        eventService.fireEvent(event);
    }

    @SubscribeMessage
    public void handleHandshake(InboundMessageContext<Handshake> input) {
        Handshake handshake = input.getMessage();

        if (handshake.getType() == Handshake.Type.SERVER) {

            ServerInfo serverInfo = toServerInfo(handshake.getProperties());
            registerServer(input, serverInfo);
        }
    }

    @SubscribeMessage
    public void handleRedirection(InboundMessageContext<Redirect> input) {
        Redirect redirect = input.getMessage();

        UUID playerUUID = redirect.getPlayerUUID();
        UUID serverId = redirect.getServerKey();

        ConnectedServerStub server = container.getConnectedServerExact(serverId);

        if (server == null) {
            log.info("§4Server by key '{}' is not connected", serverId);
            return;
        }

        UUID currentserverId = playersOnServersConnectionService.getPlayerCurrentServerKey(playerUUID)
                .orElse(null);

        if (!serverId.equals(currentserverId)) {
            playersOnServersConnectionService.insert(playerUUID, serverId);
            input.writeCallback(new Redirect.Success(playerUUID, serverId));
        } else {
            input.writeCallback(new Redirect.Failure(playerUUID, serverId));
        }
    }

    @SubscribeMessage
    public void handle(Disconnect disconnect) {
        if (disconnect.getType() == Disconnect.Type.SERVER) {
            doServerDisconnect(disconnect);
        }
    }

    private void registerServer(InboundMessageContext<Handshake> input, ServerInfo serverInfo) {
        UUID serverId = container.getExactServerKey(serverInfo.getName());

        if (serverId == null) {
            ConnectedServerStub server = createServer(input, serverInfo);
            serverId = container.registerServer(server);

            doSuccessRegister(serverId, server);
            callServerHandshakeEvent(input.getMessage(), server);

            log.info("Server §2{} §rwas registered now by key: §2{}", serverInfo.getName(), serverId);
            input.writeCallback(new Handshake.Success(serverId));

        } else {
            log.info("§4Server {} has already registered by key: {}", serverInfo.getName(), serverId);
            input.writeCallback(new Handshake.Failure(serverId));
        }
    }

    private void doSuccessRegister(UUID serverId, ConnectedServerStub server) {
        server.setUniqueId(serverId);
        server.getChannel().setProperty(EntityServer.CHANNEL_PROPERTY, server);
    }

    private ConnectedServerStub createServer(InboundMessageContext<Handshake> input, ServerInfo serverInfo) {
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
    
    private void doServerDisconnect(Disconnect disconnect) {
        UUID serverId = disconnect.getUuid();
        ConnectedServerStub server = container.getConnectedServerExact(serverId);

        if (server != null) {
            callServerDisconnectEvent(server);
            container.unregisterServer(serverId);
        }
    }
}
