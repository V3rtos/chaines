package me.moonways.bridgenet.test.mtp;

import me.moonways.bridgenet.model.bus.message.CreateGame;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MtpTransferCreateGameTest {

    private static final CreateGame DEF_MESSAGE = new CreateGame("Эмоциональные качели", "Детская площадка с программистом", 2, 1);
    private static final byte[] EXPECTED_BYTES = {0, 0, 0, 39, -48, -83, -48, -68, -48, -66, -47, -122, -48, -72, -48, -66, -48, -67, -48, -80, -48, -69, -47, -116, -48, -67, -47, -117, -48, -75, 32, -48, -70, -48, -80, -47, -121, -48, -75, -48, -69, -48, -72, 0, 0, 0, 61, -48, -108, -48, -75, -47, -126, -47, -127, -48, -70, -48, -80, -47, -113, 32, -48, -65, -48, -69, -48, -66, -47, -119, -48, -80, -48, -76, -48, -70, -48, -80, 32, -47, -127, 32, -48, -65, -47, -128, -48, -66, -48, -77, -47, -128, -48, -80, -48, -68, -48, -68, -48, -72, -47, -127, -47, -126, -48, -66, -48, -68, 0, 0, 0, 2, 0, 0, 0, 1};

    @Test
    public void test_toBytes() {
        MessageTransfer messageTransfer = MessageTransfer.encode(DEF_MESSAGE);
        messageTransfer.buf();

        byte[] transferedBytesArray = messageTransfer.getBytes();

        assertArrayEquals(transferedBytesArray, EXPECTED_BYTES);
    }

    @Test
    public void test_fromBytes() {
        MessageTransfer messageTransfer = MessageTransfer.decode(EXPECTED_BYTES);

        CreateGame input = new CreateGame();
        messageTransfer.unbuf(input);

        assertEquals(input, DEF_MESSAGE);
    }
}
