import me.moonways.bridgenet.message.BridgenetMessageHandler;
import me.moonways.bridgenet.message.MessageContainer;

public class ServerHandler extends BridgenetMessageHandler {

    public ServerHandler(MessageContainer messageContainer) {
        super(messageContainer);
    }

    public void initialize() {
        addHandler(TestMessage.class, testMessage -> {

        });

    }
}
