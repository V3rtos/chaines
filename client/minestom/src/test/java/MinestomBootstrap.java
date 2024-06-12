import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.client.minestom.MinestomServer;

@Log4j2
public class MinestomBootstrap {

    public static void main(String[] args) {
        log.info("Starting minestom-server...");
        new MinestomServer().start();
    }
}
