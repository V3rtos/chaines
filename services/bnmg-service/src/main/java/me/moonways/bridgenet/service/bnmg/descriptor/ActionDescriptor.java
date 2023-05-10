package me.moonways.bridgenet.service.bnmg.descriptor;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ActionDescriptor {

    private final byte id;
    private final String content;
}
