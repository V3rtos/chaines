package me.moonways.bridgenet.api.command;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.CommandParameter;
import me.moonways.bridgenet.api.command.annotation.Permission;
import me.moonways.bridgenet.api.command.annotation.repeatable.RepeatableCommandAliases;
import me.moonways.bridgenet.api.command.children.CommandChild;
import me.moonways.bridgenet.api.command.children.CommandChildrenScanner;
import me.moonways.bridgenet.api.command.children.definition.CommandProducerChild;
import me.moonways.bridgenet.api.command.option.CommandParameterMatcher;
import me.moonways.bridgenet.api.command.wrapper.WrappedCommand;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.FactoryType;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Autobind
public final class CommandRegistry {

    private static final String COMMAND_NOT_ANNOTATED_ERROR_MESSAGE = "Command {} is not annotated by @Command";

    private final Map<String, WrappedCommand> commandWrapperMap = new HashMap<>();

    private final CommandChildrenScanner childService = new CommandChildrenScanner();
    private final InternalCommandFactory factory = new InternalCommandFactory();

    @Inject
    private AnnotationInterceptor interceptor;
    @Inject
    private BeansService beansService;

    public void registerCommand(@NotNull Object object) {
        if (!matchesAnnotation(object)) {
            log.error(COMMAND_NOT_ANNOTATED_ERROR_MESSAGE, object.getClass().getSimpleName());
            return;
        }

        final String commandName = findCommandName(object);

        final String permission = findPermission(object);

        final Object commandProxy = toProxy(object);

        final List<CommandChild> childrenList = findChildren(object);
        final List<CommandParameterMatcher> optionsList = findOptions(object);
        final CommandSession.HelpMessageView helpMessageView = createHelpMessageView(childrenList);

        WrappedCommand commandWrapper = factory.createCommandWrapper(commandProxy, commandName, permission, childrenList,
                optionsList, helpMessageView);

        commandWrapperMap.put(commandName.toLowerCase(), commandWrapper);

        for (String alias : findAliases(object))
            commandWrapperMap.put(alias.toLowerCase(), commandWrapper);

        log.debug("Command §7'{}' §rwas success registered", object.getClass().getSimpleName());
    }

    private String findCommandName(Object comandObject) {
        Command annotation = comandObject.getClass().getDeclaredAnnotation(Command.class);
        return annotation.value();
    }

    private String findPermission(Object commandObject) {
        Permission annotation = commandObject.getClass().getDeclaredAnnotation(Permission.class);
        return annotation == null ? null : annotation.value();
    }

    private List<CommandParameterMatcher> findOptions(Object commandObject) {
        Annotation[] declaredAnnotations = commandObject.getClass().getDeclaredAnnotations();

        BeanFactory objectFactory = FactoryType.DEFAULT.get();

        return Arrays.stream(declaredAnnotations)
                .filter(annotation -> annotation.annotationType().equals(CommandParameter.class))
                .map(annotation -> (CommandParameter) annotation)
                .map(option -> objectFactory.create(option.value()))
                .collect(Collectors.toList());
    }

    private List<String> findAliases(Object commandObject) {
        Class<?> commandClass = commandObject.getClass();
        List<String> result = new ArrayList<>();

        RepeatableCommandAliases repeatableAliasesAnnotation = commandClass.getDeclaredAnnotation(RepeatableCommandAliases.class);
        if (repeatableAliasesAnnotation != null) {
            result.addAll(
                    Arrays.stream(repeatableAliasesAnnotation.value())
                            .map(Alias::value)
                            .map(String::toLowerCase)
                            .collect(Collectors.toList()));
        } else if (commandClass.isAnnotationPresent(Alias.class)) {
            result.add(commandClass.getDeclaredAnnotation(Alias.class).value().toLowerCase());
        }

        return result;
    }

    private Object toProxy(Object commandObject) {
        Bean bean = beansService.createBean(commandObject);
        beansService.tryOverrideDecorators(bean);
        beansService.inject(bean);
        return bean;
    }

    private CommandSession.HelpMessageView createHelpMessageView(List<CommandChild> childrenList) {
        final CommandSession.HelpMessageView helpMessageView = new CommandSession.HelpMessageView();
        childrenList.stream()
                .filter(child -> child instanceof CommandProducerChild)
                .map(child -> (CommandProducerChild) child)
                .forEachOrdered(child -> helpMessageView.addDescription(child.getName(), child.getUsage(), child.getDescription()));

        return helpMessageView;
    }

    private boolean matchesAnnotation(@NotNull Object object) {
        return object.getClass().isAnnotationPresent(Command.class);
    }

    public WrappedCommand getCommandWrapper(@NotNull String name) {
        return commandWrapperMap.get(name.toLowerCase());
    }

    public String[] getRegisteredCommandsArray() {
        return commandWrapperMap.keySet().toArray(new String[0]);
    }

    private List<CommandChild> findChildren(@NotNull Object object) {
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
