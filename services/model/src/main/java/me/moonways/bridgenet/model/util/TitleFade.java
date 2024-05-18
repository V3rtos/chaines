package me.moonways.bridgenet.model.util;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public class TitleFade {

    private int fadeIn;
    private int stay;
    private int fadeOut;
}
