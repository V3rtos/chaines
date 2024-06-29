package me.moonways.bridgenet.rest4j.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.rest4j.Ok;

import java.util.List;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class OkServersList implements Ok {

    private final List<String> list;
}
