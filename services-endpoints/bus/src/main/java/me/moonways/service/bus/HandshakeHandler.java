package me.moonways.service.bus;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.connection.server.ServerManager;
import me.moonways.bridgenet.api.connection.server.type.VelocityServer;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.Inject;
//import me.moonways.service.entity.server.protocol.handshake.VelocityHandshakeMessage;

import java.net.InetSocketAddress;

@MessageHandler
@Log4j2
public class HandshakeHandler extends AbstractHandshakeHandler {

    @Inject
    private ServerManager serverManager;

    @PostFactoryMethod
    private void init() {
        super.setServerManager(serverManager);
    }

   //@MessageTrigger
   //public void handle(VelocityHandshakeMessage message) {
   //    String serverName = message.getServerName();
   //    String host = message.getHost();
   //    int port = message.getPort();

   //    VelocityServer velocityServer = createVelocityServer(message.getChannel(), serverName, host, port);
   //    addServer(message, velocityServer);

   //    log.info("Velocity {} successful registered", serverName);
   //}

    private VelocityServer createVelocityServer(BridgenetChannel channel, String serverName,
                                                String host, int port) {

        InetSocketAddress serverAddress = new InetSocketAddress(host, port);
        return new VelocityServer(serverName,
                channel,
                serverAddress);
    }
}
