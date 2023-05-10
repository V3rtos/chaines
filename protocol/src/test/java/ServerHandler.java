import me.moonways.bridgenet.protocol.message.MessageHandler;
import me.moonways.bridgenet.protocol.message.MessageTrigger;

@MessageHandler
public class ServerHandler {

    @MessageTrigger
    public void handle(TestMessage testMessage) {
    }
}
