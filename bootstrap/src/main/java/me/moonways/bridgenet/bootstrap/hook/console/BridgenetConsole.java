package me.moonways.bridgenet.bootstrap.hook.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.CommandExecutionException;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
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

    @Override
    protected boolean isRunning() {
        return true; // TODO: 07.05.2023
    }

    @Override
    protected void runCommand(String commandLine) {
        if (commandLine.equalsIgnoreCase("exit")) {
            bootstrap.shutdown();
            return;
        }

        try {
            commandExecutor.execute(consoleSender, commandLine);
        }
        catch (CommandExecutionException exception) {
            log.warn("§4That command is not found: §c{}", exception.toString());
        }
    }

    @Override
    protected void shutdown() {
        bootstrap.shutdown();
    }
}
