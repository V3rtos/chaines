package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.service.players.Player;
import me.moonways.bridgenet.model.service.servers.EntityServer;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PlayerPostRedirectEvent {

    private final Player player;
    private final EntityServer server;
}
