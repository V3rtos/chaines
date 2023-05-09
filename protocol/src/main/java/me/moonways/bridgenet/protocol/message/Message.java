package me.moonways.bridgenet.protocol.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@RequiredArgsConstructor
@Getter
public class Message {

    @Setter
    private boolean responsible = false;

    @Setter
    private int messageId = 0;

    @Setter
    private int responseId = 0;

    public int getResponseMessageId() {
        return 0;
    }
}
