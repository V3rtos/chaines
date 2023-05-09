package me.moonways.bridgenet.auth;

import me.moonways.bridgenet.api.BridgenetControl;
import me.moonways.bridgenet.api.module.AbstractModule;
import me.moonways.bridgenet.api.module.ModuleIdentifier;

@ModuleIdentifier(id = "1", name = "auth", version = "1.0")
public final class AuthenticationModule extends AbstractModule {

    @Override
    public void onEnable() {
        BridgenetControl bridgenetControl = getBridgenetControl();
    }
}
