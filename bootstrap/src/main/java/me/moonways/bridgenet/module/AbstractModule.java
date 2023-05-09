package me.moonways.bridgenet.module;

import lombok.Getter;
import me.moonways.bridgenet.BridgenetControl;
import me.moonways.bridgenet.dependencyinjection.Inject;

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
