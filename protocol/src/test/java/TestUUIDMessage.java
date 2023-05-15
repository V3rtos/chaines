import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.Message;
import me.moonways.bridgenet.protocol.transfer.ByteTransfer;
import me.moonways.bridgenet.protocol.transfer.provider.TransferSerializeProvider;

import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TestUUIDMessage extends Message {

    @ByteTransfer(provider = TransferSerializeProvider.class)
    private UUID uuid;
}
