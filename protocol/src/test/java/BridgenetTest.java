import me.moonways.bridgenet.protocol.Bridgenet;
import me.moonways.bridgenet.protocol.BridgenetChannel;
import me.moonways.bridgenet.protocol.BridgenetNetty;
import me.moonways.bridgenet.protocol.BridgenetServer;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.message.MessageParameter;
import me.moonways.bridgenet.protocol.pipeline.BridgenetSettings;

public class BridgenetTest {

    public static void main(String[] args) {
        System.setProperty(Bridgenet.DEFAULT_HOST_PROPERTY, "localhost");
        System.setProperty(Bridgenet.DEFAULT_PORT_PROPERTY, "8080");

        Bridgenet bridgenet = Bridgenet.createByProperties();

        BridgenetServer server = Bridgenet.newServerBuilder(bridgenet)
                .setChannelFactory(BridgenetNetty.createServerChannelFactory())
                .setSettings(BridgenetSettings.newBuilder(bridgenet)
                        .setBridgenetMessageHandlerFactory(ServerHandler::new)
                        .build())
                .setGroup(BridgenetNetty.createEventLoopGroup(2),
                        BridgenetNetty.createEventLoopGroup(4))
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
