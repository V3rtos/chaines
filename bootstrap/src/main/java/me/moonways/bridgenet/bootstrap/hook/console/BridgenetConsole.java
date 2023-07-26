package me.moonways.bridgenet.bootstrap.hook.console;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.api.injection.Inject;
import me.moonways.service.api.command.Command;
import me.moonways.service.api.command.CommandRegistry;
import me.moonways.service.api.command.CommandSenderSession;
import me.moonways.service.api.command.exception.CommandNotFoundException;
import me.moonways.service.api.command.sender.ConsoleCommandSender;
import net.minecrell.terminalconsole.SimpleTerminalConsole;

@Log4j2
@RequiredArgsConstructor
public class BridgenetConsole extends SimpleTerminalConsole {

    @Inject
    private ConsoleCommandSender consoleSender;

    @Inject
    private CommandRegistry commandRegistry;

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
        bootstrap.shutdown();
    }
}
