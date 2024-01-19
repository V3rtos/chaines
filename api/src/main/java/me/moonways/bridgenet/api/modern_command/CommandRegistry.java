package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationProxy;
import me.moonways.bridgenet.api.modern_command.annotation.value.Aliases;
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
        CommandInfo commandInfo = createCommandInfo(object, aliases.value());

        List<SubcommandInfo> subcommands = convertFrom(CommandReflectionUtil.getMethodsOfSubcommands(object));
        commandInfo.setSubcommands(subcommands);

        return commandInfo;
    }

    private CommandInfo createCommandInfo(@NotNull Object parent, @NotNull String[] aliases) {
        return new CommandInfo(interceptCommandMethods(parent), aliases);
    }

    private List<SubcommandInfo> convertFrom(List<Method> methods) {
        List<SubcommandInfo> subcommands = new ArrayList<>();

        for (Method method : methods) {
            Aliases aliases = CommandReflectionUtil.getAliasesAnnotation(method);

            subcommands.add(new SubcommandInfo(method, aliases.value()));
        }

        return subcommands;
    }
}
