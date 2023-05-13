package me.moonways.bridgenet.protocol.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MessageState {

    REQUEST("Request message"),
    RESPONSE("Response message"),
    EMPTY("Empty message");

    private final String message;
}
