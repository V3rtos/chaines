package me.moonways.endpoint.bus;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.mtp.message.inject.MessageHandler;
import me.moonways.model.servers.ServersServiceModel;

@MessageHandler
@Log4j2
public class HandshakeHandler extends AbstractHandshakeHandler {

    @Inject
    private ServersServiceModel serverManager;

    @PostConstruct
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

    //private EntityServer createVelocityServer(BridgenetChannel channel, String serverName,
    //                                          String host, int port) {
    //
    //    InetSocketAddress serverAddress = new InetSocketAddress(host, port);
    //    return new VelocityServer(serverName,
    //            channel,
    //            serverAddress);
    //}
}
