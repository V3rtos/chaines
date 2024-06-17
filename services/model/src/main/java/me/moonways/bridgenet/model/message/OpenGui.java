package me.moonways.bridgenet.model.message;

import lombok.*;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.gui.GuiDescription;
import me.moonways.bridgenet.model.service.gui.item.ItemStack;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;
import me.moonways.bridgenet.mtp.transfer.provider.SerializeProvider;
import me.moonways.bridgenet.mtp.transfer.provider.ToJsonProvider;
import me.moonways.bridgenet.mtp.transfer.provider.ToUUIDProvider;

import java.util.List;
import java.util.UUID;

@Getter
@ToString
@ServerMessage
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor(onConstructor_ = @Inject)
public class OpenGui {

    @Getter
    @Builder
    @ToString
    public static class GuiItemView {

        private final int slot;
        private final ItemStack itemStack;
    }

    @ByteTransfer(provider = ToUUIDProvider.class)
    private UUID playerId, guiId;

    @ByteTransfer(provider = ToJsonProvider.class)
    private GuiDescription description;

    @ByteTransfer(provider = ToJsonProvider.class)
    private List<GuiItemView> items;
}
