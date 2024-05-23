package me.moonways.bridgenet.model.players.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.players.Player;

@Getter
@Builder
@ToString
public class PlayerJoinEvent {
    private final Player player;
}
