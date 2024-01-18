package me.moonways.bridgenet.endpoint.servers;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.Handshake;
import me.moonways.bridgenet.model.servers.ServerFlag;
import me.moonways.bridgenet.model.servers.ServerInfo;
import me.moonways.bridgenet.mtp.message.InputMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.MessageTrigger;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

@Log4j2
@RequiredArgsConstructor
public class ServersInputMessagesListener {

    private final ServersContainer container;

    @Inject
    private DependencyInjection injector;

    @MessageTrigger
    public void handle(InputMessageContext<Handshake> input) {
        Handshake handshake = input.getMessage();

        if (handshake.getType() == Handshake.Type.SERVER) {

            ServerInfo serverInfo = toServerInfo(handshake.getProperties());
            registerServer(input, serverInfo);
        }
    }

    private void registerServer(InputMessageContext<Handshake> input, ServerInfo serverInfo) {
        UUID serverKey = container.getServerKey(serverInfo.getName());

        if (serverKey == null) {
            ConnectedServerStub server = createServer(input, serverInfo);
            serverKey = container.registerServer(server);

            server.setUniqueId(serverKey);

            log.info("Server §2{} §rwas registered now by key: §2{}", serverInfo.getName(), serverKey);
            input.answer(new Handshake.Success(serverKey));

        } else {
            log.info("§4Server {} has already registered by key: {}", serverInfo.getName(), serverKey);
            input.answer(new Handshake.Failure(serverKey));
        }
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
