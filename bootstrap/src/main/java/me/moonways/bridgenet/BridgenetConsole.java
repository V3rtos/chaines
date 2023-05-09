package me.moonways.bridgenet;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.command.Command;
import me.moonways.bridgenet.command.CommandExecutorSession;
import me.moonways.bridgenet.command.exception.CommandNotFoundException;
import me.moonways.bridgenet.command.sender.ConsoleSender;
import me.moonways.bridgenet.dependencyinjection.Inject;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@RequiredArgsConstructor
public class BridgenetConsole extends SimpleTerminalConsole {

    private final BridgenetBootstrap bootstrap;

    @Inject
    private ConsoleSender consoleSender;

    @Override
    protected boolean isRunning() {
        return true; // TODO: 07.05.2023
    }

    @Override
    protected void runCommand(String commandLine) {
        BridgenetControl bridgenetControl = bootstrap.getBridgenetControl();
        CommandExecutorSession commandExecutorSession = new CommandExecutorSession(consoleSender);

        try {
            String[] arguments = commandLine.split("\b");

            Command command = bridgenetControl.getCommand(getCommandName(arguments));
            command.executeCommand(arguments, commandExecutorSession);
        }
        catch (CommandNotFoundException exception) {
            System.out.println("Command not found!");
        }
    }

    private String getCommandName(String[] arguments) {
        return arguments[0];
    }

    @Override
    protected void shutdown() {
        // TODO: 09.05.2023
    }
}
