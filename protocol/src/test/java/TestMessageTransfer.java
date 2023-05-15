import me.moonways.bridgenet.protocol.transfer.MessageTransfer;

import java.util.Arrays;
import java.util.UUID;

public class TestMessageTransfer {

    public static void main(String[] args) {
        UUID uuid = UUID.randomUUID();

        System.out.println("Original UUID: " + uuid); // a4c41198-4b52-4fef-b081-f336d77a08da
        System.out.println("Original UUID Most bits: " + uuid.getMostSignificantBits()); // -6574110210257694737
        System.out.println("Original UUID Least bits: " + uuid.getLeastSignificantBits()); // -5728029834169677606

        MessageTransfer messageTransfer = new MessageTransfer(new TestUUIDMessage(uuid), null);

        // buffer
        messageTransfer.buf();

        byte[] transferUuidBytes = messageTransfer.getBytes();
        System.out.println("Buffer UUID Bytes: " + Arrays.toString(transferUuidBytes)); // [-92, -60, 17, -104, 75, 82, 79, -17, -80, -127, -13, 54, -41, 122, 8, -38]
        System.out.println("Buffer UUID Bytes Length: " + transferUuidBytes.length); // 16

        // unbuffer
        TestUUIDMessage message = new TestUUIDMessage();
        messageTransfer.unbuf(message);

        System.out.println("Unbuffer UUID Bytes: " + message.getUuid()); // a4c41198-4b52-4fef-b081-f336d77a08da
    }
}
