package me.moonways.bridgenet.test.connections.mtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferUuidProvider;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class BytesTransferringOfUuidTest {

    private static final String UUID_STRING = "a4c41198-4b52-4fef-b081-f336d77a08da";
    private static final byte[] EXPECTED_BYTES = {-92, -60, 17, -104, 75, 82, 79, -17, -80, -127, -13, 54, -41, 122, 8, -38};

    private UUID uuid;

    @Before
    public void setUp() {
        uuid = new UUID(-6574110210257694737L, -5728029834169677606L);
    }

    @Test
    @TestOrdered(1)
    public void test_validUuid() {
        assertEquals(uuid.toString(), UUID_STRING);
    }

    @Test
    @TestOrdered(2)
    public void test_toBytes() {
        MessageTransfer messageTransfer = MessageTransfer.encode(new TestUuidMessage(uuid));
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

        TestUuidMessage message = new TestUuidMessage();
        messageTransfer.unbuf(message);

        assertEquals(message.getUuid().toString(), UUID_STRING);
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestUuidMessage {

        @ByteTransfer(provider = TransferUuidProvider.class)
        private UUID uuid;
    }
}
