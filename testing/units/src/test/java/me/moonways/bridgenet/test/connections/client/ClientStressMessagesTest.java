package me.moonways.bridgenet.test.connections.client;

import me.moonways.bridgenet.client.api.data.UserDto;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.model.message.SendCommand;
import me.moonways.bridgenet.mtp.channel.BridgenetNetworkChannel;
import me.moonways.bridgenet.test.data.ExampleClient;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.AllModules;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@RunWith(ModernTestEngineRunner.class)
@TestModules(AllModules.class)
public class ClientStressMessagesTest {

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    private static final int PACKETS_LENGTH = 1000;
    private static final int SLEEPING_TIMEOUT = PACKETS_LENGTH * 10;

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijtklmnopqrstuvwxyz1234567890";

    private final ExampleClient subj = new ExampleClient();

    @Before
    public void setUp() {
        subj.start();
    }

    @Test
    public void test_stressingPacketsRouting() throws InterruptedException {
        BridgenetNetworkChannel channel = subj.getChannel();
        for (int count = 0; count < PACKETS_LENGTH; count++) {
            processRandomPlayer(channel);
        }

        Thread.sleep(SLEEPING_TIMEOUT);
    }

    private void processRandomPlayer(BridgenetNetworkChannel channel) {
        UUID playerID = UUID.randomUUID();

        channel.sendAwait(Handshake.Success.class,
                new Handshake(Handshake.Type.PLAYER,
                        UserDto.builder()
                                .uniqueId(playerID)
                                .proxyId(UUID.randomUUID())
                                .name(randomizeString(1000))
                                .build().toProperties())

        ).whenComplete((success, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
                return;
            }
            channel.send(new SendCommand(playerID, "memory"));
        });
    }

    private static String randomizeString(int length) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < length; i++) {
            builder.append(ALPHABET.charAt(RANDOM.nextInt(0, ALPHABET.length())));
        }

        return builder.toString();
    }
}
