package me.moonways.bridgenet.api.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.Permission;
import me.moonways.bridgenet.api.command.children.CommandChild;
import me.moonways.bridgenet.api.command.children.CommandChildrenScanner;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

@Log4j2
public final class CommandRegistry {

    private static final String COMMAND_NOT_ANNOTATED_ERROR_MESSAGE = "Command {} is not annotated by @Command";

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
            log.error(COMMAND_NOT_ANNOTATED_ERROR_MESSAGE, object.getClass().getSimpleName());
            return;
        }

        Command command = object.getClass().getDeclaredAnnotation(Command.class);
        Permission permission = object.getClass().getDeclaredAnnotation(Permission.class);

        String commandName = command.value();

        List<CommandChild> childrenList = createChildren(object);

        dependencyInjection.injectFields(object);
        Object proxiedObject = interceptor.createProxy(object, new DecoratedObjectProxy());

        commandWrapperMap.put(commandName, new CommandWrapper(
                commandName,
                permission == null ? null : permission.value(),
                proxiedObject,
                childrenList));

        log.info("Command §7'{}' §rwas success registered", object.getClass().getSimpleName());
    }

    public CommandWrapper getCommandWrapper(@NotNull String name) {
        return commandWrapperMap.get(name.toLowerCase());
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
