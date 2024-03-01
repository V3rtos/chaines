package me.moonways.bridgenet.endpoint.servers.handler;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.event.EventHandle;
import me.moonways.bridgenet.api.event.EventService;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.endpoint.servers.ServersContainer;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.event.ServerDisconnectEvent;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.mtp.event.ChannelDownstreamEvent;

import java.rmi.RemoteException;
import java.util.Optional;

@RequiredArgsConstructor
public class ServersDownstreamListener {

    private final ServersContainer container;

    @Inject
    private EventService eventService;

    private void callServerDisconnectEvent(EntityServer entityServer) {
        ServerDisconnectEvent event = new ServerDisconnectEvent(ServerDisconnectEvent.DownstreamType.DOWNSTREAM, entityServer);
        eventService.fireEvent(event);
    }

    @EventHandle
    public void handle(ChannelDownstreamEvent event) throws RemoteException {
        BridgenetNetworkChannel channel = event.getChannel();

        Optional<EntityServer> serverProperty = channel.getProperty(EntityServer.CHANNEL_PROPERTY);

        if (serverProperty.isPresent()) {
            EntityServer entityServer = serverProperty.get();

            callServerDisconnectEvent(entityServer);
            container.unregisterServer(entityServer.getUniqueId());
        }
    }
}
