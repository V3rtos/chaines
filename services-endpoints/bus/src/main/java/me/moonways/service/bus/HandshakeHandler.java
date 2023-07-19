package me.moonways.service.bus;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.mtp.message.inject.MessageHandler;
import me.moonways.service.api.entities.server.BridgenetServers;

@MessageHandler
@Log4j2
public class HandshakeHandler extends AbstractHandshakeHandler {

    @Inject
    private BridgenetServers serverManager;

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
}
