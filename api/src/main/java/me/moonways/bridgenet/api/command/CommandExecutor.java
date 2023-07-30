package me.moonways.bridgenet.api.command;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.Matcher;
import me.moonways.bridgenet.api.command.annotation.Mentor;
import me.moonways.bridgenet.api.command.annotation.Producer;
import me.moonways.bridgenet.api.command.children.definition.MentorChild;
import me.moonways.bridgenet.api.command.children.definition.ProducerChild;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@Log4j2
public final class CommandExecutor {

    private static final String MENTOR_CHILD_NOT_FOUND_MESSAGE = "Couldn't find @Mentor in command {}";

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

    public void execute(@NotNull EntityCommandSender sender, @NotNull String label) {
        String name = lookupName(label);
        String[] args = lookupArguments(label, 1);

        CommandWrapper commandWrapper = commandRegistry.getCommandWrapper(name);

        if (commandWrapper == null) {
            sender.sendMessage("§cCommand not found");
            return;
        }

        CommandSession mentorSession = createSession(sender, args);

        if (matchesBeforeExecute(mentorSession, commandWrapper)) { //if access is allowed
            if (mentorSession.getArguments().isEmpty()) {
                if (!matchesPermission(sender, commandWrapper.getPermission())) {
                    return;
                }

                fireMentor(commandWrapper, mentorSession);
                return;
            }

            fireProducer(commandWrapper, mentorSession, args);
        }
    }

    private void fireProducer(@NotNull CommandWrapper commandWrapper,
                              @NotNull CommandSession commandSession,
                              @NotNull String[] args) {
        EntityCommandSender sender = commandSession.getSender();

        ProducerChild producerChild = commandWrapper.<ProducerChild>find(Producer.class)
                .filter(producer -> producer.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);

        if (producerChild == null) {
            fireMentor(commandWrapper, commandSession);
            return;
        }

        String permission = producerChild.getPermission();

        CommandSession childSession = createSession(sender, lookupArguments(args, 1));

        if (permission == null || matchesPermission(sender, permission)) {
            fireChild(producerChild, commandWrapper, childSession);
        }
    }

    private void fireMentor(CommandWrapper commandWrapper, CommandSession session) {
        MentorChild mentorChild = commandWrapper.<MentorChild>find(Mentor.class)
                .findFirst()
                .orElse(null);

        if (mentorChild == null) {
            log.error(MENTOR_CHILD_NOT_FOUND_MESSAGE, commandWrapper.getCommandName());
            return;
        }

        invokeMethod(session, commandWrapper.getSource(), mentorChild.getMethod());
    }

    private void fireChild(ProducerChild child, CommandWrapper commandWrapper, CommandSession session) {
        invokeMethod(session, commandWrapper.getSource(), child.getMethod());
    }

    private boolean matchesPermission(@NotNull EntityCommandSender sender, @NotNull String permission) {
        boolean hasPermission = sender.hasPermission(permission);

        if (!hasPermission)
            sender.sendMessage("§cYou do not have permission to execute this command");

        return hasPermission;
    }

    private boolean matchesBeforeExecute(@NotNull CommandSession session, @NotNull CommandWrapper wrapper) {
        long numberOfUnauthorizedAccesses = wrapper.find(Matcher.class).filter(predicate ->
                Boolean.FALSE.equals(invokeMethod(session, wrapper.getSource(), predicate.getMethod()))).count();

        return numberOfUnauthorizedAccesses == 0;
    }

    private Object invokeMethod(@NotNull CommandSession session,
                                @NotNull Object source,
                                @NotNull Method method) {

        return interceptor.callProxiedMethod(source, method, new Object[]{session});
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

    @SuppressWarnings("SameParameterValue")
    private String[] lookupArguments(@NotNull String label, int copyOfRange) {
        String[] args = lookupArguments(label);

        return lookupArguments(args, copyOfRange);
    }

    private String[] lookupArguments(@NotNull String[] args, int copyOfRange) {
        return Arrays.copyOfRange(args, copyOfRange, args.length);
    }
}
