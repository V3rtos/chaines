package me.moonways.bridgenet.api.modern_command.service;

import me.moonways.bridgenet.api.container.MapContainer;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.*;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationService;
import me.moonways.bridgenet.api.modern_command.data.Command;
import me.moonways.bridgenet.api.modern_command.data.CommandInfo;
import me.moonways.bridgenet.api.modern_command.data.Subcommand;
import me.moonways.bridgenet.api.modern_command.util.CommandReflectionUtil;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Autobind
public class CommandRegistrationManagement {

    //todo мб написать класс для нормальной работы со строкой? чтобы было правильно, например там хранить сразу имя команды, аргументы

    private static final String ANNOTATION_NOT_FOUND = "Annotation @%s in %s %s not found";
    private static final String NONE_METHODS_FOUND = "None of the methods are annotated with an annotation @%s";

    @Inject
    private MapContainer<String, CommandInfo> container;

    @Inject
    private CommandAnnotationService annotationService;

    private void add(String commandName, CommandInfo commandInfo) {
        container.add(commandName.toLowerCase(), commandInfo);
    }

    private void addAll(String[] aliases, CommandInfo commandInfo) {
        for (String alias : aliases) {
            container.add(alias, commandInfo);
        }
    }

    public void unregister(String commandName) {
        container.remove(commandName);
    }

    public void unregisterAll() {
        container.removeAll();
    }

    @SuppressWarnings("unchecked")
    public  <T extends CommandInfo> T get(String commandName) {
        return (T) container.get(commandName);
    }

    public void register(Object object) {
        Class<?> parentCls = object.getClass();

        registerCommand(object, parentCls);
        registerSubcommands(object, parentCls);
        registerHelpCommand(object, parentCls);
    }

    private void registerCommand(Object object, Class<?> parentCls) {
        Method handle = CommandReflectionUtil.getMethodBy(parentCls, Parent.class);
        validateExistMethod(handle, Parent.class);

        Aliases aliases = CommandReflectionUtil.getAnnotation(handle, Aliases.class);
        validateAnnotatedMethod(handle, aliases);

        CommandInfo commandInfo = new Command(object, handle, aliases.value());
        registerAnnotations(commandInfo, parentCls);
        registerAnnotations(commandInfo, handle);

        addAll(aliases.value(), commandInfo);
    }

    private void registerSubcommand(Object parent, Method handle) {
        Aliases aliases = CommandReflectionUtil.getAnnotation(handle, Aliases.class);
        validateAnnotatedMethod(handle, aliases);

        registerSubcommand(parent, handle, aliases.value());
    }

    private void registerSubcommand(Object parent, Method handle, String[] aliases) {
        CommandInfo commandInfo = new Subcommand(parent, handle, aliases);
        registerAnnotations(commandInfo, handle);

        addAll(aliases, commandInfo);
    }

    private void registerSubcommands(Object parent, Class<?> parentCls) {
        List<Method> handlers = CommandReflectionUtil.getMethodsBy(parentCls, Aliases.class)
                .stream()
                .filter(method -> !method.isAnnotationPresent(Parent.class))
                .collect(Collectors.toList());

        for (Method handle : handlers) {
            registerSubcommand(parent, handle);
        }
    }

    private void registerHelpCommand(Object parent, Class<?> parentCls) {
        Method handle = CommandReflectionUtil.getMethodBy(parentCls, Help.class);
        validateExistMethod(handle, Help.class);

        registerSubcommand(parent, handle, new String[]{"help", "помощь", "?"});
    }

    private void validateAnnotatedMethod(Method method, Annotation annotation) {
        if (annotation == null) {
            throw new CommandException(String.format(ANNOTATION_NOT_FOUND, Aliases.class.getName(), "method", method.getName()));
        }
    }

    private void validateExistMethod(Method method, Class<? extends Annotation> annotationCls) {
        if (method == null) {
            throw new CommandException(String.format(NONE_METHODS_FOUND, annotationCls.getName()));
        }
    }

    private void registerAnnotations(CommandInfo commandInfo, AnnotatedElement annotatedElement) {
        annotationService.modifyAll(commandInfo, annotatedElement);
    }
}
