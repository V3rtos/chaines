package me.moonways.bridgenet.rest.wrapper;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class TotalOnline {

    private final int value;
}
