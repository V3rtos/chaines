package me.moonways.bridgenet.model.players;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public class Title {

    private String title;
    private String subtitle;

    private int fadeIn;
    private int stay;
    private int fadeOut;
}
