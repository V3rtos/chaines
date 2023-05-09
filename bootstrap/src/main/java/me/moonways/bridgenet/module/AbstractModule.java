package me.moonways.bridgenet.module;

import lombok.Getter;
import me.moonways.bridgenet.command.CommandRegistry;
import me.moonways.bridgenet.dependencyinjection.Inject;
import me.moonways.bridgenet.event.EventService;
import me.moonways.bridgenet.scheduler.Scheduler;

@Getter
public abstract class AbstractModule implements Module {

    @Inject
    private CommandRegistry commandRegistry;

    @Inject
    private EventService eventService;

    @Inject
    private Scheduler scheduler;

    @Override
    public void onEnable() {
        // override me.
    }

    @Override
    public void onDisable() {
        // override me.
    }
}
