package me.moonways.service.api.command;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
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

    public void execute(@NotNull EntityCommandSender sender, @NotNull String label) {
        String name = lookupName(label);
        String[] args = lookupArguments(label, 1);

        CommandWrapper commandWrapper = commandRegistry.getCommandWrapper(name);

        if (commandWrapper == null) {
            sender.sendMessage("Command not found");
            return;
        }

        final CommandSession mentorSession = createSession(sender, args);

        if (matches(mentorSession, commandWrapper)) { //if access is allowed
            if (mentorSession.getArguments().isEmpty()) {
                fireMentor(commandWrapper, mentorSession);
                return;
            }

            ProducerChild producerChild = commandWrapper.<ProducerChild>find(Producer.class)
                    .filter(producer -> producer.getName().equalsIgnoreCase(args[0]))
                    .findFirst()
                    .orElse(null);

            if (producerChild == null) {
                fireMentor(commandWrapper, mentorSession);
                return;
            }

            final String permission = producerChild.getPermission();

            if (permission != null && sender.hasPermission(permission)) {

                final CommandSession childSession = createSession(sender, Arrays.copyOfRange(args, 1, args.length));
                fireChild(producerChild, commandWrapper, childSession);
            }
        }
    }

    private void fireMentor(CommandWrapper commandWrapper, CommandSession session) {
        MentorChild mentorChild = commandWrapper.<MentorChild>find(Mentor.class)
                .findFirst()
                .orElse(null);

        invokeMethod(session, commandWrapper.getSource(), mentorChild.getMethod());
    }

    private void fireChild(ProducerChild child, CommandWrapper commandWrapper, CommandSession session) {
        invokeMethod(session, commandWrapper.getSource(), child.getMethod());
    }

    private boolean matches(@NotNull CommandSession session, @NotNull CommandWrapper wrapper) {
        long numberOfUnauthorizedAccesses = wrapper.find(Predicate.class).filter(predicate ->
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

    private String[] lookupArguments(@NotNull String label, int copyOfRange) {
        String[] args = lookupArguments(label);

        return Arrays.copyOfRange(args, copyOfRange, args.length);
    }
}
