package me.moonways.bridgenet.rest4j.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.rest4j.Ok;

import java.util.UUID;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OkServer implements Ok {

    private final String name;
    private final UUID id;

    private final int online;
}
