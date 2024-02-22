package me.moonways.bridgenet.api.modern_command.execution;

import lombok.Getter;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_command.InjectCommand;
import me.moonways.bridgenet.api.modern_command.args.CommandArgument;
import me.moonways.bridgenet.api.modern_command.args.CommandArgumentImpl;
import me.moonways.bridgenet.api.modern_command.Command;
import me.moonways.bridgenet.api.modern_command.depend.cooldown.dao.CooldownDao;
import me.moonways.bridgenet.api.modern_command.CommandContainer;
import me.moonways.bridgenet.api.modern_command.execution.verify.CommandVerifyResult;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.execution.verify.CommandVerifyManagement;
import me.moonways.bridgenet.api.modern_command.session.CommandSessionImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class CommandManagementServiceImpl implements CommandManagementService {

    @Inject
    private CommandVerifyManagement verifyManagement;

    @Inject
    private DependencyInjection injector;

    @Inject
    @Getter
    private CooldownDao cooldownDao;

    @Inject
    private CommandContainer commandContainer;

    @Inject
    private CommandRegistrationManagement registrationManagement;

    @PostConstruct
    private void initCommands() {
        injector.peekAnnotatedMembers(InjectCommand.class);
        //todo to new injector
    }

    @Override
    public void handle(EntityCommandSender entity, String line) {
        CommandArgument commandArgument = CommandArgumentImpl.withLine(line);
        handle(entity, commandArgument);
    }

    @Override
    public void handle(EntityCommandSender entity, CommandArgument commandArgument) {
        String commandName = commandArgument.getCommandName(); //todo работа с аргументами совсем не очень, переписать всё

        if (!isExists(commandName)) {
            //handleFail(entity, String.format(CommandExceptionMessages.NOT_FOUND, "Команда не найдена")); //todo
            return;
        }

        ArgumentParser argumentParser = new ArgumentParser();
        String completedName = argumentParser.getCommandName(commandArgument);

        //Пытаемся выполнить команду
        performAccess(completedName, entity, commandArgument);
    }

    private void performAccess(String commandName, EntityCommandSender entity, CommandArgument commandArgument) {
        Command command = get(commandName);
        CommandSession session = createSession(command, commandName, entity, commandArgument);

        CommandVerifyResult result = verifyManagement.validate(session);

        if (result.isFail()) {
            handleFail(session.getEntity(), result.getMessage());
            return;
        }

        handleOk(session);
    }

    private void handleFail(EntityCommandSender entity, String message) {
        entity.sendMessage(message);
    }

    private void handleOk(CommandSession session) {
        invoke(session, session.getCommand());
    }

    @Override
    public void unregister(String name) {
        registrationManagement.unregister(name);
    }

    @Override
    public void unregisterAll() {
        registrationManagement.unregisterAll();
    }

    @Override
    public void register(Object object) {
        registrationManagement.register(object);
    }

    private CommandSession createSession(Command command, String commandName, EntityCommandSender entity,
                                         CommandArgument commandArgument) {
        return new CommandSessionImpl(command, commandName, entity, commandArgument, cooldownDao);
    }

    private Command get(String commandName) {
        return commandContainer.get(commandName);
    }

    private boolean isExists(String name) {
        return commandContainer.get(name) != null;
    }

    public void invoke(CommandSession session, Command command) {
        Method handle = command.getHandle();

        try {
            handle.invoke(session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    private class ArgumentParser {

        private String getCommandName(CommandArgument commandArgument) {
            if (commandArgument.size() == 1) {
                return commandArgument.getCommandName();
            }

            if (commandArgument.get(1).isPresent()) {
                String subcommandName = commandArgument.get(1).get();

                if (commandArgument.size() > 1 && isExists(subcommandName)) {
                    return subcommandName;
                }
            }

            return null;
        }
    }
}
