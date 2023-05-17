package me.moonways.bridgenet.velocity.bridgenet;

import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;
import me.moonways.bridgenet.service.inject.Inject;
import me.moonways.bridgenet.services.connection.server.ServerConnectResponseType;
import me.moonways.bridgenet.services.connection.server.message.handshake.response.HandshakeResponseMessage;
import me.moonways.bridgenet.velocity.BridgenetVelocity;
import me.moonways.bridgenet.velocity.exception.ServerConnectException;

@MessageHandler
public class VelocityBridgenetChannelHandler {

    private static final String ALREADY_CONNECTED = "Already connected";
    private static final String SUCCESSFUL_CONNECTED = "Successful connected";
    private static final String BAD_RESPONSE = "Bad response";

    @Inject
    private BridgenetVelocity bridgenetVelocity;

    @MessageTrigger
    public void handle(HandshakeResponseMessage responseMessage) {
        int result = responseMessage.getResult();

        ServerConnectResponseType serverConnectResponseType = ServerConnectResponseType.of(result);

        switch (serverConnectResponseType) {
            case SUCCESSFUL_CONNECTED: {
                bridgenetVelocity.getLogger().info(ALREADY_CONNECTED);
                break;
            }

            case ALREADY_CONNECTED: {
                throw new ServerConnectException(SUCCESSFUL_CONNECTED);
            }

            case BAD_RESPONSE: {
                throw new ServerConnectException(BAD_RESPONSE);
            }
        }
    }
}
