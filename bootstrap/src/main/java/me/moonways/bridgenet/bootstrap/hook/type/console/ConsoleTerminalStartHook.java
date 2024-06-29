package me.moonways.bridgenet.bootstrap.hook.type.console;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import org.jetbrains.annotations.NotNull;

@Log4j2
public class ConsoleTerminalStartHook extends BootstrapHook {
    @Inject
    private BeansService beansService;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) {
        BridgenetConsole bridgenetConsole = new BridgenetConsole();

        beansService.inject(bridgenetConsole);

        log.info("§2BridgeNet was completed for using!");
        Threads.newSingleThreadExecutor().submit(bridgenetConsole::start);
    }
}
