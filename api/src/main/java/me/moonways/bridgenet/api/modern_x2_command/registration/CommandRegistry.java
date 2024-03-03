package me.moonways.bridgenet.api.modern_x2_command.registration;

import me.moonways.bridgenet.api.container.MapContainerImpl;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.modern_x2_command.Command;

@Autobind
public class CommandRegistry extends MapContainerImpl<String, Command> {

    public void remove(Class<?> cls) {
        for (Command command : values()) {
            if (command.getBean().getType().getRoot().equals(cls)) {
                remove(command.getInfo().getUid());
            }
        }
    }
}
