import io.netty.channel.ChannelHandlerContext;
import me.moonways.bridgenet.message.BridgenetMessageHandler;
import me.moonways.bridgenet.message.MessageContainer;
import me.moonways.bridgenet.message.MessageRegistry;
import org.jetbrains.annotations.NotNull;

public class ServerHandler extends BridgenetMessageHandler {

    public ServerHandler(MessageContainer messageContainer) {
        super(messageContainer);
    }

    public void initialize() {
        addHandler(TestMessage.class, testMessage -> {

        });

    }
}
