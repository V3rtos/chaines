package me.moonways.bridgenet.api.modern_x2_command.install;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.Command;
import me.moonways.bridgenet.api.modern_x2_command.CommandInfo;
import me.moonways.bridgenet.api.modern_x2_command.Sub;
import me.moonways.bridgenet.api.modern_x2_command.exception.CommandException;
import me.moonways.bridgenet.api.modern_x2_command.install.reflect.ReflectionUtil;
import me.moonways.bridgenet.api.modern_x2_command.install.registry.CommandRegistry;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class CommandInstallationService {

    @Inject
    private CommandRegistry registry;

    public void install(Class<?> parentCls, Object parent) {
        List<Command> commands = createCommands(parentCls, parent);

        for (Command command : commands) {
            registry.add(command.getInfo().getUid(), command);
        }
    }

    private List<Command> createCommands(Class<?> parentCls, Object parent) {
        List<Command> commands = new ArrayList<>();

        for (Method method : ReflectionUtil.find(parentCls, Sub.class)) {
            Sub sub = ReflectionUtil.get(parentCls, Sub.class);
            validateAnnotation(sub);

            for (String name : sub.value()) {
                commands.add(createCommand(parent, method, name));
            }
        }

        return commands;
    }

    private Command createCommand(Object parent, Method handle, String commandName) {
        return createCommand(parent, handle, createCommandInfo(commandName));
    }

    private Command createCommand(Object parent, Method handle, CommandInfo commandInfo) {
        return new Command(parent, handle, commandInfo);
    }

    private CommandInfo createCommandInfo(String name) {
        return new CommandInfo(name);
    }

    private void validateAnnotation(Annotation annotation) {
        if (annotation == null) {
            throw new CommandException("Can't find annotation " + Sub.class.getSimpleName());
        }
    }
}
