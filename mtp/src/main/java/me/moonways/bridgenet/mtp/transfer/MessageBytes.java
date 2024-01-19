package me.moonways.bridgenet.mtp.transfer;

import java.util.Arrays;

import lombok.*;
import lombok.experimental.NonFinal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class MessageBytes {

    public static MessageBytes create(byte[] rawArray) {
        return new MessageBytes(rawArray);
    }

    @NonFinal
    private int position;

    private final byte[] rawArray;

    public void moveTo(int add) {
        this.position += add;
    }

    public byte[] getArray() {
        return Arrays.copyOfRange(rawArray, position, rawArray.length);
    }
}
