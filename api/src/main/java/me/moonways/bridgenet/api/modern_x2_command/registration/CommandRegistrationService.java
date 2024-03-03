package me.moonways.bridgenet.api.modern_x2_command.registration;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.Bean;
import me.moonways.bridgenet.api.inject.bean.BeanMethod;
import me.moonways.bridgenet.api.inject.bean.service.BeansScanningService;
import me.moonways.bridgenet.api.modern_x2_command.*;
import me.moonways.bridgenet.api.modern_x2_command.ai.AICommandService;
import me.moonways.bridgenet.api.modern_x2_command.ai.AINativeCommandContext;
import me.moonways.bridgenet.api.modern_x2_command.exception.CommandException;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Autobind
public class CommandRegistrationService {

    @Inject
    private CommandRegistry registry;

    @Inject
    private BeansScanningService scanningService;

    @Inject
    private AICommandService aiCommandService;

    public void register(Bean bean) {
        registerGeneral(bean);
        registerSub(bean);
    }

    public void register(Class<?> parentCls, Object parent) {
        Bean bean = scanningService.createBean(parentCls, parent);
        register(bean);
    }

    private void registerGeneral(Bean bean) {
        Command general = createGeneralCommand(bean);
        prepare(general);

        registry.add(general.getInfo().getUid(), general);
    }

    private void registerSub(Bean bean) {
        List<Command> commands = createSubcommands(bean);

        for (Command command : commands) {
            prepare(command);

            registry.add(command.getInfo().getUid(), command);
        }
    }

    public void unregisterAll() {
        registry.removeAll();
        aiCommandService.removeAll();
    }

    public void unregister(Class<?> cls) {
        registry.remove(cls);
    }

    private void prepare(Command command) {
        Method root = command.getBeanMethod().getRoot();

        aiCommandService.prepare(AINativeCommandContext.create(root, command.getInfo()));
    }

    private Command createCommand(Bean bean, BeanMethod beanMethod, String commandName, @Nullable String accessKey) {
        return createCommand(bean, beanMethod, createCommandInfo(commandName, accessKey));
    }

    private Command createCommand(Bean bean, BeanMethod beanMethod, CommandInfo commandInfo) {
        return new Command(bean, beanMethod, commandInfo);
    }

    private List<Command> createSubcommands(Bean bean, BeanMethod beanMethod) {
        String accessKey = getAccessKey(beanMethod);

        return beanMethod.getAnnotation(SubCommand.class)
                .map(SubCommand::value)
                .map(Arrays::asList)
                .map(list -> list.stream().map(alias -> createCommand(bean, beanMethod, alias, accessKey))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private List<Command> createSubcommands(Bean bean) {
        List<Command> commands = new ArrayList<>();

        for (BeanMethod beanMethod : bean.getType().getAllDeclaredFunctionsByAnnotation(SubCommand.class)) {
            commands.addAll(createSubcommands(bean, beanMethod));
        }

        return commands;
    }

    private Command createGeneralCommand(Bean bean) {
        BeanMethod beanMethod = bean.getType().getAllDeclaredFunctionsByAnnotation(GeneralCommand.class)
                .stream().findFirst().orElseThrow(CommandException::new);

        return createGeneralCommand(bean, beanMethod);
    }

    private Command createGeneralCommand(Bean bean, BeanMethod beanMethod) {
        String accessKey = getAccessKey(beanMethod);

        return beanMethod.getAnnotation(GeneralCommand.class)
                .map(GeneralCommand::value)
                .map(value -> createCommand(bean, beanMethod, value, accessKey))
                .orElse(null);
    }

    private CommandInfo createCommandInfo(String name, @Nullable String accessKey) {
        return new CommandInfo(name, accessKey);
    }

    private String getAccessKey(BeanMethod beanMethod) {
        return beanMethod.getAnnotation(CommandAccessKey.class)
                .map(CommandAccessKey::value)
                .orElse(null);
    }
}
