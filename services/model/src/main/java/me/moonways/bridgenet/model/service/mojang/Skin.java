package me.moonways.bridgenet.model.service.mojang;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class Skin {

    private final String nickname;

    private final String texture;
    private final String signature;
}
