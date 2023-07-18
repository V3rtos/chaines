package me.moonways.bridgenet.bootstrap.hook.console;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.injection.Inject;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class ApacheConsoleStopHook extends BootstrapHook {

    @Inject
    private BridgenetConsole bridgenetConsole;

    @Override
    protected void postExecute(@NotNull AppBootstrap bootstrap) {
        bridgenetConsole.shutdown();
    }
}
