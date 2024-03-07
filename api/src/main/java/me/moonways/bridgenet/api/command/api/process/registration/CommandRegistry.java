package me.moonways.bridgenet.api.command.api.process.registration;

import me.moonways.bridgenet.api.container.MapContainerImpl;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.command.api.uses.Command;

import java.util.UUID;

@Autobind
public class CommandRegistry extends MapContainerImpl<UUID, Command> {

    public void remove(Class<?> cls) {
        for (Command command : values()) {
            if (command.getBean().getType().getRoot().equals(cls)) {
                remove(command.getInfo().getUid());
            }
        }
    }
}
