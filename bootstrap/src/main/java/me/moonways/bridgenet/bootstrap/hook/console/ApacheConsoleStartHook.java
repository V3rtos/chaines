package me.moonways.bridgenet.bootstrap.hook.console;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.api.injection.DependencyInjection;
import me.moonways.bridgenet.api.injection.Inject;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class ApacheConsoleStartHook extends BootstrapHook {

    @Inject
    private DependencyInjection dependencyInjection;

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        BridgenetConsole bridgenetConsole = new BridgenetConsole();
        dependencyInjection.bind(bridgenetConsole);

        bridgenetConsole.start();
    }
}
