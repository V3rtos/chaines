package me.moonways.bridgenet.velocity.connection;

import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.ServerConnectResponseType;
import me.moonways.bridgenet.services.connection.server.message.HandshakeResponseMessage;
import me.moonways.bridgenet.velocity.BridgenetVelocity;
import me.moonways.bridgenet.velocity.exception.ServerConnectException;

@MessageHandler
public class VelocityChannelHandler {

    @Inject
    private BridgenetVelocity bridgenetVelocity;

    @MessageTrigger
    public void handle(HandshakeResponseMessage responseMessage) {
        int result = responseMessage.getResult();

        ServerConnectResponseType serverConnectResponseType = ServerConnectResponseType.of(result);

        switch (serverConnectResponseType) {
            case SUCCESSFUL_CONNECTED: {
                bridgenetVelocity.getLogger().info("successful connected");
                break;
            }

            case ALREADY_CONNECTED: {
                throw new ServerConnectException("already connected");
            }

            case BAD_RESPONSE: {
                throw new ServerConnectException("bad response");
            }
        }
    }
}
