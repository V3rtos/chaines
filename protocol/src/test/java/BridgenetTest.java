import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import me.moonways.bridgenet.*;
import me.moonways.bridgenet.pipeline.BridgenetConfiguration;

public class BridgenetTest {

    public static void main(String[] args) {
        Bridgenet bridgenet = Bridgenet
                .of(System.getProperty("bridgenet.address.host"),
                        Integer.getInteger("bridgenet.address.port"));

        BridgenetServer server = Bridgenet.ServerBuilder
                .newBuilder()
                .build();

        for (BridgenetChannelExecutor executor : server.getExecutors()) {
            executor.sendMessage(new Message(), MessageParameter.Builder
                    .newBuilder()
                    .setCallback(false)
                    .build());
        }

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
