package me.moonways.bridgenet.model.util.audience.event;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.model.util.audience.MessageDirection;
import me.moonways.bridgenet.model.util.audience.EntityAudience;
import net.kyori.adventure.text.Component;

@Getter
@Builder
@ToString
public class AudienceSendEvent {

    private final EntityAudience entity;
    private final Component[] components;
    private final MessageDirection direction;
}
