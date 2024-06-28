package me.moonways.bridgenet.test.mtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import me.moonways.bridgenet.model.message.CreateGame;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BytesTransferringOfCreateGameTest {

    private static final CreateGame DEF_MESSAGE = new CreateGame("Эмоциональные качели", "Детская площадка с программистом", 2, 1);
    private static final byte[] EXPECTED_BYTES = {0, 0, 0, 39, -48, -83, -48, -68, -48, -66, -47, -122, -48, -72, -48, -66, -48, -67, -48, -80, -48, -69, -47, -116, -48, -67, -47, -117, -48, -75, 32, -48, -70, -48, -80, -47, -121, -48, -75, -48, -69, -48, -72, 0, 0, 0, 61, -48, -108, -48, -75, -47, -126, -47, -127, -48, -70, -48, -80, -47, -113, 32, -48, -65, -48, -69, -48, -66, -47, -119, -48, -80, -48, -76, -48, -70, -48, -80, 32, -47, -127, 32, -48, -65, -47, -128, -48, -66, -48, -77, -47, -128, -48, -80, -48, -68, -48, -68, -48, -72, -47, -127, -47, -126, -48, -66, -48, -68, 0, 0, 0, 2, 0, 0, 0, 1};

    @Test
    @TestOrdered(1)
    public void test_toBytes() {
        MessageTransfer messageTransfer = MessageTransfer.encode(DEF_MESSAGE);
        messageTransfer.buf();

        byte[] transferedBytesArray = ByteCodec.readBytesArray(messageTransfer.getByteBuf());

        assertArrayEquals(transferedBytesArray, EXPECTED_BYTES);
    }

    @Test
    @TestOrdered(2)
    public void test_fromBytes() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(EXPECTED_BYTES);

        MessageTransfer messageTransfer = MessageTransfer.decode(byteBuf);

        CreateGame input = new CreateGame();
        messageTransfer.unbuf(input);

        assertEquals(input, DEF_MESSAGE);
    }
}
