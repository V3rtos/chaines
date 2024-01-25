package me.moonways.bridgenet.api.modern_command.service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_command.InjectCommand;
import me.moonways.bridgenet.api.modern_command.annotation.AbstractCommandAnnotationHandler;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationService;
import me.moonways.bridgenet.api.modern_command.args.ArgumentWrapper;
import me.moonways.bridgenet.api.modern_command.args.ArgumentWrapperImpl;
import me.moonways.bridgenet.api.modern_command.cooldown.dao.CooldownDao;
import me.moonways.bridgenet.api.modern_command.data.CommandContainer;
import me.moonways.bridgenet.api.modern_command.data.CommandInfo;
import me.moonways.bridgenet.api.modern_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.session.CommandSessionImpl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

public class CommandManagementServiceImpl implements CommandManagementService {

    private static final String COMMAND_NOT_FOUND_MSG = "Bridgenet | %s";

    @Inject
    private DependencyInjection injector;

    @Inject
    @Getter
    private CooldownDao cooldownDao;

    @Inject
    private CommandContainer commandContainer;

    @Inject
    private CommandRegistrationManagement registrationManagement;

    @Inject
    private CommandAnnotationService annotationService;

    @PostConstruct
    private void initCommands() {
        injector.peekAnnotatedMembers(InjectCommand.class);
        //todo to new injector
    }

    @Override
    public void handle(EntityCommandSender entity, String line) {
        ArgumentWrapper argumentWrapper = ArgumentWrapperImpl.withLine(line);
        handle(entity, argumentWrapper);
    }

    @Override
    public void handle(EntityCommandSender entity, ArgumentWrapper argumentWrapper) {
        String commandName = argumentWrapper.getParentName();

        if (!isExists(commandName)) {
            handleFailed(entity, String.format(COMMAND_NOT_FOUND_MSG, "Команда не найдена"));
            return;
        }

        //Парсим нужное имя команды в массиве аргументов
        ArgumentParser argumentParser = new ArgumentParser();
        String completedName = argumentParser.getName(argumentWrapper);

        //Пытаемся выполнить команду
        performAccess(completedName, entity, argumentWrapper);
    }

    private void performAccess(String commandName, EntityCommandSender entity, ArgumentWrapper argumentWrapper) {
        CommandSession session = createSession(commandName, entity, argumentWrapper);
        CommandInfo commandInfo = get(commandName);

        AccessManagement accessManagement = new AccessManagement(commandInfo, session);

        if (!accessManagement.isAllowed()) {
            Optional<AbstractCommandAnnotationHandler.Result> result = accessManagement.getFailedResult();

            if (!result.isPresent()) return;

            handleFailed(session.getEntity(), result.get().getMessage());
            return;
        }

        handleSuccess(session, commandInfo);
    }

    private void handleFailed(EntityCommandSender entity, String message) {
        entity.sendMessage(message);
    }

    private void handleSuccess(CommandSession session, CommandInfo commandInfo) {
        invoke(session, commandInfo);
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

    private CommandSession createSession(String commandName, EntityCommandSender entity, ArgumentWrapper argumentWrapperHelper) {
        return new CommandSessionImpl(commandName, entity, argumentWrapperHelper, cooldownDao);
    }

    private CommandInfo get(String commandName) {
        return commandContainer.get(commandName);
    }

    private boolean isExists(String name) {
        return commandContainer.get(name) != null;
    }

    public void invoke(CommandSession session, CommandInfo commandInfo) {
        Method handle = commandInfo.getHandle();

        try {
            handle.invoke(session);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    class ArgumentParser {

        private String getName(ArgumentWrapper argumentWrapper) {
            if (argumentWrapper.size() == 1) {
                return argumentWrapper.getParentName();
            }

            if (argumentWrapper.get(1).isPresent()) {
                String subcommandName = argumentWrapper.get(1).get();

                if (argumentWrapper.size() > 1 && isExists(subcommandName)) {
                    return subcommandName;
                }
            }

            return null;
        }
    }

    @RequiredArgsConstructor
    class AccessManagement {

        private final CommandInfo commandInfo;
        private final CommandSession session;

        private List<AbstractCommandAnnotationHandler.Result> getFailedResults() {
            return annotationService.getFailedResults(commandInfo, session);
        }

        public Optional<AbstractCommandAnnotationHandler.Result> getFailedResult() {
            return getFailedResults()
                    .stream()
                    .map(Optional::of)
                    .findFirst()
                    .orElse(Optional.empty());
        }

        public boolean isAllowed() {
            return getFailedResults().isEmpty();
        }
    }
}
