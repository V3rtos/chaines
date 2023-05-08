package me.moonways.bridgenet.transfer;

import java.util.Arrays;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.Value;
import lombok.experimental.NonFinal;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Value(staticConstructor = "create")
public class MessageBytes {

    @NonFinal
    int position;

    byte[] rawArray;

    public void addPosition(int add) {
        this.position += add;
    }

    public byte[] getArray() {
        return Arrays.copyOfRange(rawArray, position, rawArray.length);
    }
}
