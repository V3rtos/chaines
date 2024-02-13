package me.moonways.bridgenet.test.mtp;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.Unpooled;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.mtp.transfer.ByteCodec;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.MessageTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.TransferPropertiesProvider;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Properties;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MtpTransferPropertiesTest {

    private static final String KEY = "output.message.text";
    private static final String VALUE = "Hello World!";

    private static final byte[] EXPECTED_BYTES = {0, 0, 0, 1, 0, 0, 0, 19, 111, 117, 116, 112, 117, 116, 46, 109, 101, 115, 115, 97, 103, 101, 46, 116, 101, 120, 116, 0, 0, 0, 12, 72, 101, 108, 108, 111, 32, 87, 111, 114, 108, 100, 33};

    private Properties properties;

    @Before
    public void setUp() {
        properties = new Properties();
        properties.setProperty(KEY, VALUE);
    }

    @Test
    public void test_toBytes() {
        MessageTransfer messageTransfer = MessageTransfer.encode(new TestPropertiesMessage(properties));
        messageTransfer.buf();

        byte[] transferedBytesArray = ByteCodec.readBytesArray(messageTransfer.getByteBuf());

        assertArrayEquals(transferedBytesArray, EXPECTED_BYTES);
    }

    @Test
    public void test_fromBytes() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeBytes(EXPECTED_BYTES);

        MessageTransfer messageTransfer = MessageTransfer.decode(byteBuf);

        TestPropertiesMessage message = new TestPropertiesMessage();
        messageTransfer.unbuf(message);

        assertEquals(message.getProperties().getProperty(KEY), VALUE);
    }

    @Getter
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    private static class TestPropertiesMessage {

        @ByteTransfer(provider = TransferPropertiesProvider.class)
        private Properties properties;
    }
}
