package me.moonways.bridgenet.model.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.transfer.ByteTransfer;

import java.util.List;

@ClientMessage
@NoArgsConstructor(onConstructor_ = @Inject)
public class GetCommands {

    @Getter
    @ToString
    @ClientMessage
    @AllArgsConstructor
    @NoArgsConstructor(onConstructor_ = @Inject)
    public static class Result {

        @ByteTransfer
        private List<String> list;
    }
}
