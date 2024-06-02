package me.moonways.endpoint.players.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.model.message.Disconnect;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.model.service.bus.HandshakePropertiesConst;
import me.moonways.bridgenet.model.service.players.Player;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;
import me.moonways.endpoint.players.PlayerStoreStub;

import java.rmi.RemoteException;
import java.util.Properties;
import java.util.UUID;

@Log4j2
@InboundMessageListener
@RequiredArgsConstructor
public final class InboundPlayerConnectionListener {

    private final PlayerStoreStub playerStoreStub;

    @SubscribeMessage
    public void handle(InboundMessageContext<Handshake> context) throws RemoteException {
        Handshake handshake = context.getMessage();

        if (handshake.getType() == Handshake.Type.PLAYER) {

            Properties properties = handshake.getProperties();
            String userUuidProperty = properties.getProperty(HandshakePropertiesConst.USER_UUID);

            if (userUuidProperty == null) {
                return;
            }

            Player player = playerStoreStub.addOnlinePlayer(properties);

            if (player != null) {
                context.callback(new Handshake.Success(UUID.fromString(userUuidProperty)));
                log.info("§6Player({}, '{}') connected to Bridgenet", player.getName(), player.getId());
            } else {
                context.callback(new Handshake.Failure(UUID.fromString(userUuidProperty)));
            }
        }
    }

    @SubscribeMessage
    public void handle(Disconnect disconnect) throws RemoteException {
        if (disconnect.getType() == Disconnect.Type.PLAYER) {
            Player player = playerStoreStub.removeOnlinePlayer(disconnect.getUuid());

            if (player != null) {
                log.info("§4Player({}, '{}') disconnected", player.getName(), player.getId());
            }
        }
    }
}
