package me.moonways.bridgenet;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.command.CommandExecutorSession;
import me.moonways.bridgenet.command.exception.CommandAccessDeniedException;
import me.moonways.bridgenet.command.exception.CommandNotFoundException;
import net.minecrell.terminalconsole.SimpleTerminalConsole;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;

@RequiredArgsConstructor
public class BridgenetConsole extends SimpleTerminalConsole {

    private final BridgenetBootstrap bootstrap;
    private final ConsoleSender consoleSender = new ConsoleSender();

    @Override
    protected boolean isRunning() {
        return true; // TODO: 07.05.2023
    }

    @Override
    protected void runCommand(String command) {
        String[] arguments = command.split(" ");

        CommandExecutorSession commandExecutorSession = new CommandExecutorSession(consoleSender);

        try {
            bootstrap.getCommandRegistrationService().getCommandContainer().getCommand(getCommandName(arguments))
                    .executeCommand(arguments, commandExecutorSession);

        } catch (CommandNotFoundException exception) {
            System.out.println("Command not found!");
        } catch (CommandAccessDeniedException exception) {
            System.out.println("Don't have permission");
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
