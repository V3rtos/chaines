package me.moonways.service.api.command;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.service.api.command.annotation.Command;
import me.moonways.service.api.command.children.CommandChild;
import me.moonways.service.api.command.exception.CommandNotAnnotatedException;
import me.moonways.service.api.command.exception.CommandNotFoundException;
import me.moonways.service.api.command.children.CommandChildrenScanner;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@Log4j2
public final class CommandRegistry {

    private static final String COMMAND_NOT_ANNOTATED_ERROR = "Command %s is not annotated";

    private final Map<String, CommandWrapper> commandWrapperMap = new HashMap<>();

    private final CommandChildrenScanner childService = new CommandChildrenScanner();

    @Inject
    private AnnotationInterceptor interceptor;

    @Inject
    private DependencyInjection dependencyInjection;

    private boolean matchAnnotation(@NotNull Object object) {
        return object.getClass().isAnnotationPresent(Command.class);
    }

    public void registerCommand(@NotNull Object object) {
        if (!matchAnnotation(object)) {
            log.error(new CommandNotAnnotatedException(String.format(COMMAND_NOT_ANNOTATED_ERROR, object.getClass().getName())));
            return;
        }

        Command command = object.getClass().getDeclaredAnnotation(Command.class);
        String commandName = command.value();

        List<CommandChild> childrenList = createChildren(object);

        dependencyInjection.injectFields(object);
        Object proxiedObject = interceptor.createProxy(object, new DecoratedObjectProxy());

        commandWrapperMap.put(commandName,
                new CommandWrapper(commandName, proxiedObject, childrenList));

        log.info("Command §7'{}' §rwas success registered", object.getClass().getSimpleName());
    }

    public CommandWrapper getCommandWrapper(@NotNull String name) {
        try {
            return commandWrapperMap.get(name.toLowerCase());
        }
        catch (CommandNotFoundException exception) {
            log.error(exception);
        }

        return null;
    }

    private List<CommandChild> createChildren(@NotNull Object object) {
        List<CommandChild> childrenList = new ArrayList<>();

        childrenList.addAll(childService.findProducerChildren(object));
        childrenList.addAll(childService.findPredicateChildren(object));

        CommandChild mentorChild = childService.findMentorChild(object);

        if (mentorChild == null) {
            log.error("§4Mentor command child is not found in {}", object.getClass().getName());

            return Collections.emptyList();
        }

        childrenList.add(mentorChild);
        return childrenList;
    }
}
