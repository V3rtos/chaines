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
import me.moonways.bridgenet.api.command.children.definition.ProducerChild;
import me.moonways.bridgenet.api.command.wrapper.WrappedCommand;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

@Log4j2
public final class CommandRegistry {

    private static final String COMMAND_NOT_ANNOTATED_ERROR_MESSAGE = "Command {} is not annotated by @Command";

    private final Map<String, WrappedCommand> commandWrapperMap = new HashMap<>();

    private final CommandChildrenScanner childService = new CommandChildrenScanner();
    private final InternalCommandFactory factory = new InternalCommandFactory();

    @Inject
    private AnnotationInterceptor interceptor;

    @Inject
    private DependencyInjection dependencyInjection;

    public void registerCommand(@NotNull Object object) {
        if (!matchesAnnotation(object)) {
            log.error(COMMAND_NOT_ANNOTATED_ERROR_MESSAGE, object.getClass().getSimpleName());
            return;
        }

        final String commandName = findCommandName(object);

        final String permission = findPermission(object);

        final Object commandProxy = toProxy(object);

        final List<CommandChild> childrenList = createChildren(object);
        final CommandSession.HelpMessageView helpMessageView = createHelpMessageView(childrenList);

        WrappedCommand commandWrapper = factory.createCommandWrapper(commandProxy, commandName, permission, childrenList, helpMessageView);
        commandWrapperMap.put(commandName, commandWrapper);

        log.info("Command §7'{}' §rwas success registered", object.getClass().getSimpleName());
    }

    private String findCommandName(Object comandObject) {
        Command annotation = comandObject.getClass().getDeclaredAnnotation(Command.class);
        return annotation.value();
    }

    private String findPermission(Object commandObject) {
        Permission annotation = commandObject.getClass().getDeclaredAnnotation(Permission.class);
        return annotation == null ? null : annotation.value();
    }

    private Object toProxy(Object commandObject) {
        dependencyInjection.injectFields(commandObject);
        return interceptor.createProxy(commandObject, new DecoratedObjectProxy());
    }

    private CommandSession.HelpMessageView createHelpMessageView(List<CommandChild> childrenList) {
        final CommandSession.HelpMessageView helpMessageView = new CommandSession.HelpMessageView();
        childrenList.stream()
                .filter(child -> child instanceof ProducerChild)
                .map(child -> (ProducerChild) child)
                .forEachOrdered(child -> helpMessageView.addDescription(child.getName(), child.getDescription()));

        return helpMessageView;
    }

    private boolean matchesAnnotation(@NotNull Object object) {
        return object.getClass().isAnnotationPresent(Command.class);
    }

    public WrappedCommand getCommandWrapper(@NotNull String name) {
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
