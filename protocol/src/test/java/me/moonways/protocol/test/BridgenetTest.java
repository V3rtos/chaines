package me.moonways.protocol.test;

import me.moonways.bridgenet.protocol.Bridgenet;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.BridgenetNetty;
import me.moonways.bridgenet.protocol.BridgenetServer;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageParameter;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;

@Component
public class BridgenetTest {

    @Inject
    private DependencyInjection dependencyInjection;

    private static void injectDependencies() {
        DependencyInjection dependencyInjection = new DependencyInjection();
        dependencyInjection.scanDependenciesOfBasicPackage();

        dependencyInjection.addDepend(dependencyInjection);
    }

    public static void main(String[] args) {
        injectDependencies();

        System.setProperty(Bridgenet.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(Bridgenet.DEFAULT_PORT_PROPERTY, "8080");

        Bridgenet bridgenet = Bridgenet.createByProperties();

        BridgenetServer server = Bridgenet.newServerBuilder(bridgenet)
                .setGroup(BridgenetNetty.createEventLoopGroup(2),
                        BridgenetNetty.createEventLoopGroup(4))
                .setChannelFactory(BridgenetNetty.createServerChannelFactory())
                .build();

        BridgenetChannel bridgenetChannel = server.bindSync();
        bridgenetChannel.sendMessage(new Message(), MessageParameter.newBuilder()
                .setCallback(false)
                .build());

        //MessageResponse<Message> response = server.getExecutors().sendMessage(new Message(),
        //        MessageParameter.Builder.newBuilder()
        //                .setCallback(true)
        //                .build());
        //
        //response.whenReceived((message, throwable) -> {
        //
        //});
    }
}
