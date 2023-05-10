package me.moonways.bridgenet.auth;

import me.moonways.bridgenet.service.event.EventService;
import me.moonways.bridgenet.api.module.AbstractModule;
import me.moonways.bridgenet.api.module.ModuleIdentifier;

@ModuleIdentifier(id = "auth", name = "Authentication", version = "1.0")
public final class AuthenticationModule extends AbstractModule {

    @Override
    public void onEnable() {
        registerEvents();
    }

    private void registerEvents() {
        EventService eventService = getBridgenetControl().getEventService();
        eventService.registerHandler(new AuthenticationListener());
    }
}
