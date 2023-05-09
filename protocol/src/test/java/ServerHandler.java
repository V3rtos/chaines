import me.moonways.bridgenet.protocol.message.BridgenetMessageHandler;
import me.moonways.bridgenet.protocol.message.MessageContainer;

public class ServerHandler extends BridgenetMessageHandler {

    public ServerHandler(MessageContainer messageContainer) {
        super(messageContainer);
    }

    public void initialize() {
        addHandler(TestMessage.class, testMessage -> {

        });

    }
}
