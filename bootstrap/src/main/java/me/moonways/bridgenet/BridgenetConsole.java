package me.moonways.bridgenet;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.service.api.command.Command;
import me.moonways.service.api.command.CommandRegistry;
import me.moonways.service.api.command.CommandSenderSession;
import me.moonways.service.api.command.exception.CommandNotFoundException;
import me.moonways.service.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.injection.Inject;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@Log4j2
@RequiredArgsConstructor
public class BridgenetConsole extends SimpleTerminalConsole {

    private final BridgenetBootstrap bootstrap;

    @Inject
    private ConsoleCommandSender consoleSender;

    @Inject
    private CommandRegistry commandRegistry;

    @Override
    protected boolean isRunning() {
        return true; // TODO: 07.05.2023
    }

    @Override
    protected void runCommand(String commandLine) {
        try {
            String[] arguments = commandLine.split("\b");

            CommandSenderSession commandSenderSession = new CommandSenderSession(consoleSender, arguments);

            Command command = commandRegistry.getCommandContainer().getCommand(getCommandName(arguments));
            command.executeCommand(arguments, commandSenderSession);
        }
        catch (CommandNotFoundException exception) {

            log.warn(ChatColor.RED + "Please write 'help' for a list of available commands!");
            log.warn(ChatColor.RED + exception.toString());
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
