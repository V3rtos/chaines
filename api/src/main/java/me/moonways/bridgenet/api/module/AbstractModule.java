package me.moonways.bridgenet.api.module;

import lombok.Getter;
import me.moonways.bridgenet.api.BridgenetControl;
import me.moonways.bridgenet.service.inject.Inject;

@Getter
public abstract class AbstractModule implements Module {

    @Inject
    private BridgenetControl bridgenetControl;

    @Override
    public void onEnable() {
        // override me.
    }

    @Override
    public void onDisable() {
        // override me.
    }
}
