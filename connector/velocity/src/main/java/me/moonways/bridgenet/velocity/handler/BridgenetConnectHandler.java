package me.moonways.bridgenet.velocity.handler;

import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.ServerConnectStatus;
import me.moonways.bridgenet.services.connection.server.message.HandshakeResponseMessage;
import me.moonways.bridgenet.velocity.BridgenetVelocity;
import me.moonways.bridgenet.velocity.exception.ClientConnectException;
import org.jetbrains.annotations.NotNull;

@MessageHandler
public class BridgenetConnectHandler {

    @Inject
    private BridgenetVelocity bridgenetVelocity;

    @MessageTrigger
    public void handle(@NotNull HandshakeResponseMessage message) {
        int status = message.getStatus();

        ServerConnectStatus serverConnectStatus = getConnectStatus(status);

        switch (serverConnectStatus) {
            case SUCCESS: {
                bridgenetVelocity.getLogger().info("successful connected to bridgenet");
            }

            case ALREADY_CONNECTED: {
                throw new ClientConnectException("already connected");

            }

            case BAD_RESPONSE: {
                throw new ClientConnectException("bad response");

            }
        }
    }

    private ServerConnectStatus getConnectStatus(int status) {
        return ServerConnectStatus.of(status);
    }
}
