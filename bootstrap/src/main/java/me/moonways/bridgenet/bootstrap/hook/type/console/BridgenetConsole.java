package me.moonways.bridgenet.bootstrap.hook.type.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.restart.RestartService;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@Log4j2
@RequiredArgsConstructor
public class BridgenetConsole extends SimpleTerminalConsole {

    @Inject
    private ConsoleCommandSender consoleSender;
    @Inject
    private CommandExecutor commandExecutor;
    @Inject
    private AppBootstrap bootstrap;
    @Inject
    private RestartService restartService;

    @Override
    protected boolean isRunning() {
        return bootstrap.isRunning();
    }

    @Override
    protected void shutdown() {
        // do nothing.
    }

    @Override
    protected void runCommand(String commandLine) {
        switch (commandLine) {
            case "exit": {
                bootstrap.shutdownApp();
                break;
            }
            case "restart": {
                restartService.doRestart();
                break;
            }
            default: {
                doRunCommand(commandLine);
            }
        }
    }

    private void doRunCommand(String input) {
        try {
            commandExecutor.execute(consoleSender, input);
        } catch (CommandExecutionException exception) {
            log.warn("§6That command is not found: §e{}", exception.toString());
        }
    }
}
