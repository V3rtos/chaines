package me.moonways.service.api.command;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.service.api.command.annotation.Command;
import me.moonways.service.api.command.annotation.Mentor;
import me.moonways.service.api.command.annotation.Predicate;
import me.moonways.service.api.command.annotation.Producer;
import me.moonways.service.api.command.children.definition.MentorChild;
import me.moonways.service.api.command.children.definition.ProducerChild;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Log4j2
public class CommandExecutor {

    private final CommandRegistry commandRegistry = new CommandRegistry();

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private AnnotationInterceptor interceptor;

    @PostFactoryMethod
    private void init() {
        dependencyInjection.injectFields(commandRegistry);
        dependencyInjection.findComponentsIntoBasePackage(Command.class);

        dependencyInjection.getContainer().getFoundComponents(Command.class)
                .forEach(commandRegistry::registerCommand);
    }

    @SuppressWarnings("DataFlowIssue")
    public void execute(@NotNull EntityCommandSender sender, @NotNull String label) {
        String commandName = lookupName(label);
        String[] commandArgs = lookupArguments(label, 1);

        CommandWrapper commandWrapper = commandRegistry.getCommandWrapper(commandName);

        if (commandWrapper == null) {
            sender.sendMessage("Command not found");
            return;
        }

        CommandSession session = createSession(sender, commandArgs);

        if (matches(session, commandWrapper)) { //if access is allowed
            if (commandArgs.length == 0) {
                MentorChild mentorChild = (MentorChild) commandWrapper.find(Mentor.class).findFirst().orElse(null);

                invokeMethod(
                        session,
                        commandWrapper.getProxiedSource(),
                        mentorChild.getMethod());
                return;
            }

            String childName = commandArgs[0];

            ProducerChild producerChild = commandWrapper.<ProducerChild>find(Producer.class)
                    .filter(producer -> producer.getName().equalsIgnoreCase(childName)).findFirst().orElse(null);

            String permission = producerChild.getPermission();

            if (sender.hasPermission(permission)) {
                invokeMethod(
                        session,
                        commandWrapper.getProxiedSource(),
                        producerChild.getMethod());
            }
        }
    }

    private boolean matches(@NotNull CommandSession session, @NotNull CommandWrapper wrapper) {
        long numberOfUnauthorizedAccesses = wrapper.find(Predicate.class).filter(predicate ->
                Boolean.FALSE.equals(invokeMethod(session, wrapper.getProxiedSource(), predicate.getMethod()))).count();

        return numberOfUnauthorizedAccesses == 0;
    }

    private Object invokeMethod(@NotNull CommandSession session,
                                @NotNull Object proxiedSource,
                                @NotNull Method method) {
        return interceptor.callProxiedMethod(proxiedSource, method, new Object[]{session});
    }

    private CommandSession createSession(@NotNull EntityCommandSender sender,
                                         @NotNull String[] args) {
        ArgumentArrayWrapper argumentArrayWrapper = createWrapper(args);

        return new CommandSession(sender, argumentArrayWrapper);
    }

    private ArgumentArrayWrapper createWrapper(@NotNull String[] args) {
        return new ArgumentArrayWrapper(args);
    }

    private String lookupName(@NotNull String label) {
        return label.split(" ")[0];
    }

    private String[] lookupArguments(@NotNull String label) {
        return label.split(" ");
    }

    private String[] lookupArguments(@NotNull String label, int copyOfRange) {
        String[] args = lookupArguments(label);

        return Arrays.copyOfRange(args, copyOfRange, args.length);
    }
}
