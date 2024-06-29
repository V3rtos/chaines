package me.moonways.bridgenet.rest4j.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.rest4j.Ok;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OkTotalOnline implements Ok {

    private final int value;
}
