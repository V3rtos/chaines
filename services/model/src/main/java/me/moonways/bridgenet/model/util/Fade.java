package me.moonways.bridgenet.model.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public final class Fade {

    public static Fade defaults() {
        return Fade.builder()
                .fadeIn(20)
                .stay(60)
                .fadeOut(20)
                .build();
    }

    private int fadeIn;
    private int stay;
    private int fadeOut;
}
