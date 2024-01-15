package me.moonways.bridgenet.bootstrap.hook.type.console;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class ApacheConsoleStartHook extends ApplicationBootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        BridgenetConsole bridgenetConsole = new BridgenetConsole();
        dependencyInjection.bind(bridgenetConsole);

        bridgenetConsole.start();
    }
}
