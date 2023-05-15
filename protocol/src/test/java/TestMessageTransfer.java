import me.moonways.bridgenet.protocol.transfer.MessageTransfer;

import java.util.Arrays;
import java.util.UUID;

public class TestMessageTransfer {

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();

        System.out.println("Original UUID: " + uuid);
        System.out.println("Original UUID Most bits: " + uuid.getMostSignificantBits());
        System.out.println("Original UUID Least bits: " + uuid.getLeastSignificantBits());

        MessageTransfer messageTransfer = new MessageTransfer(new TestUUIDMessage(uuid), null);

        // buffer
        messageTransfer.buf();

        byte[] transferUuidBytes = messageTransfer.getBytes();
        System.out.println("Buffer UUID Bytes: " + Arrays.toString(transferUuidBytes));
        System.out.println("Buffer UUID Bytes Length: " + transferUuidBytes.length);

        // unbuffer
        TestUUIDMessage message = new TestUUIDMessage();
        messageTransfer.unbuf(message);

        System.out.println("Unbuffer UUID Bytes: " + message.getUuid());
    }
}
