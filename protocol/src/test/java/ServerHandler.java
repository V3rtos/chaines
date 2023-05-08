import io.netty.channel.ChannelHandlerContext;
import me.moonways.bridgenet.BridgenetMessageHandler;
import me.moonways.bridgenet.MessageRegistryContainer;
import org.jetbrains.annotations.NotNull;

public class ServerHandler extends BridgenetMessageHandler {

    public ServerHandler(MessageRegistryContainer messageRegistryContainer) {
        super(messageRegistryContainer);
    }

    public static void main(String[] args) {

    }

    @Override
    public void handleChannelActive(@NotNull ChannelHandlerContext channelHandlerContext) {
        super.handleChannelActive(channelHandlerContext);
    }

    public void initialize() {
        addHandler(TestMessage.class, testMessage -> {

        });

    }
}
