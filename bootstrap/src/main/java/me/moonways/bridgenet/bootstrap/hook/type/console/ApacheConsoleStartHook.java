package me.moonways.bridgenet.bootstrap.hook.type.console;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class ApacheConsoleStartHook extends BootstrapHook {

    @Inject
    private BeansService beansService;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        //bootstrap.cleanupLogsContents();

        BridgenetConsole bridgenetConsole = new BridgenetConsole();
        beansService.bind(bridgenetConsole);

        bridgenetConsole.start();
    }
}
