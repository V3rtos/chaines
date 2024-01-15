package me.moonways.bridgenet.test.mtp;

import me.moonways.bridgenet.mtp.transfer.MessageTransfer;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MtpMessageTransferToBytesTest {

    private static final String UUID_STRING = "a4c41198-4b52-4fef-b081-f336d77a08da";
    private static final byte[] EXPECTED_BYTES = {-92, -60, 17, -104, 75, 82, 79, -17, -80, -127, -13, 54, -41, 122, 8, -38};

    private UUID uuid;

    @Before
    public void setUp() {
        uuid = new UUID(-6574110210257694737L, -5728029834169677606L);
    }

    @Test
    public void test_validUuid() {
        assertEquals(uuid.toString(), UUID_STRING);
    }

    @Test
    public void test_toBytes() {
        MessageTransfer messageTransfer = MessageTransfer.encode(new TestUuidMessage(uuid));
        messageTransfer.buf();

        byte[] transferedBytesArray = messageTransfer.getBytes();

        assertArrayEquals(transferedBytesArray, EXPECTED_BYTES);
    }

    @Test
    public void test_fromBytes() {
        MessageTransfer messageTransfer = MessageTransfer.decode(EXPECTED_BYTES);

        TestUuidMessage message = new TestUuidMessage();
        messageTransfer.unbuf(message);

        assertEquals(message.getUuid().toString(), UUID_STRING);
    }
}
