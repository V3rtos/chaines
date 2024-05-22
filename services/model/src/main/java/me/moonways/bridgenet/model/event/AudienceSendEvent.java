package me.moonways.bridgenet.model.event;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.audience.MessageDirection;
import me.moonways.bridgenet.model.audience.EntityAudience;
import net.kyori.adventure.text.Component;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AudienceSendEvent {

    private final EntityAudience entity;
    private final Component[] components;
    private final MessageDirection direction;
}
