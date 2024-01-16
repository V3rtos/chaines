package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;
import me.moonways.bridgenet.api.modern_command.modern_annotation.CommandAnnotationProxy;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Description;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.EntityLevel;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.UsageCooldown;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Permission;
import me.moonways.bridgenet.api.modern_command.reflection.CommandReflectionUtil;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Autobind
public class CommandRegistry {

    @Inject
    private AnnotationInterceptor interceptor;

    private Object interceptCommandMethods(Object command) {
        return interceptor.createProxy(command, new CommandAnnotationProxy());
    }

    public CommandInfo register(@NotNull Object object) {
        Aliases aliases = CommandReflectionUtil.getAliasesAnnotation(object);
        CommandInfo commandInfo = new CommandInfo(interceptCommandMethods(object), aliases.value());

        initCommandAnnotations(commandInfo.getParent(), commandInfo);

        List<SubcommandInfo> subcommands = methodsFrom(CommandReflectionUtil.getMethodsOfSubcommands(object));

        for (SubcommandInfo subcommandInfo : subcommands) {
            initSubcommandAnnotations(subcommandInfo.getMethod(), subcommandInfo);
        }

        commandInfo.setSubcommands(subcommands);

        return commandInfo;
    }

    private void initCommandAnnotations(Object object, StandardCommandInfo commandInfo) {
        if (CommandReflectionUtil.isExistsAnnotation(object, Permission.class)) {
            Permission permission = CommandReflectionUtil.getPermissionAnnotation(object);
            commandInfo.setPermission(permission.value());
        }

        if (CommandReflectionUtil.isExistsAnnotation(object, EntityLevel.class)) {
            EntityLevel entityLevel = CommandReflectionUtil.getEntityLevelAnnotation(object);
            commandInfo.setEntityType(entityLevel.value());

        }

        if (CommandReflectionUtil.isExistsAnnotation(object, UsageCooldown.class)) {
            UsageCooldown interval = CommandReflectionUtil.getIntervalAnnotation(object);
            commandInfo.setInterval(new IntervalInfo(interval.time(), interval.unit()));
        }

        if (CommandReflectionUtil.isExistsAnnotation(object, Description.class)) {
            Description description = CommandReflectionUtil.getDescriptionAnnotation(object);
            commandInfo.setDescription(description.value());
        }
    }

    private void initSubcommandAnnotations(Method method, StandardCommandInfo commandInfo) {
        if (CommandReflectionUtil.isExistsAnnotation(method, Permission.class)) {
            Permission permission = CommandReflectionUtil.getPermissionAnnotation(method);
            commandInfo.setPermission(permission.value());
        }

        if (CommandReflectionUtil.isExistsAnnotation(method, EntityLevel.class)) {
            EntityLevel entityLevel = CommandReflectionUtil.getEntityLevelAnnotation(method);
            commandInfo.setEntityType(entityLevel.value());

        }

        if (CommandReflectionUtil.isExistsAnnotation(method, UsageCooldown.class)) {
            UsageCooldown interval = CommandReflectionUtil.getIntervalAnnotation(method);
            commandInfo.setInterval(new IntervalInfo(interval.time(), interval.unit()));
        }

        if (CommandReflectionUtil.isExistsAnnotation(method, Description.class)) {
            Description description = CommandReflectionUtil.getDescriptionAnnotation(method);
            commandInfo.setDescription(description.value());
        }
    }

    private List<SubcommandInfo> methodsFrom(List<Method> methods) {
        List<SubcommandInfo> subcommands = new ArrayList<>();

        for (Method method : methods) {
            Aliases aliases = CommandReflectionUtil.getAliasesAnnotation(method);

            subcommands.add(new SubcommandInfo(method, aliases.value()));
        }

        return subcommands;
    }
}
