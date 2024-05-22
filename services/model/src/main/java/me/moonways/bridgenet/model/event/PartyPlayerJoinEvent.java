package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.event.Event;
import me.moonways.bridgenet.model.service.parties.Party;
import me.moonways.bridgenet.model.service.players.Player;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class PartyPlayerJoinEvent implements Event {

    private final Party party;
    private final Player joinedPlayer;
}
