package me.moonways.bridgenet.api.command;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.MatcherExecutor;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.annotation.ProducerExecutor;
import me.moonways.bridgenet.api.command.children.definition.MentorChild;
import me.moonways.bridgenet.api.command.children.definition.ProducerChild;
import me.moonways.bridgenet.api.command.option.CommandOptionMatcher;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.command.wrapper.WrappedCommand;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Log4j2
public final class CommandExecutor {

    private static final String MENTOR_CHILD_NOT_FOUND_MESSAGE = "Couldn't find @Mentor in command {}";

    private final CommandRegistry commandRegistry = new CommandRegistry();
    private final InternalCommandFactory factory = new InternalCommandFactory();

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

    public void execute(@NotNull EntityCommandSender sender, @NotNull String label) throws CommandExecutionException {
        String name = factory.findNameByLabel(label);
        String[] arguments = factory.copyArgumentsOfRange(factory.findArgumentsByLabel(label));

        WrappedCommand wrapper = commandRegistry.getCommandWrapper(name);

        if (wrapper == null) {
            throw new CommandExecutionException("Label cannot be contains command was exists");
        }

        CommandSession mentorSession = factory.createSession(wrapper.getHelpMessageView(), sender, arguments);

        fireOption(wrapper, mentorSession);

    }

    private void fireOption(WrappedCommand wrapper, CommandSession session) {
        EntityCommandSender sender = session.getSender();
        List<CommandOptionMatcher> optionList = wrapper
                .getOptionsList().stream().filter(option -> option.matches(session))
                .peek(commandOptionMatcher -> commandOptionMatcher.apply(session))
                .collect(Collectors.toList());

        if (optionList.isEmpty()) {
            if (matchesPermission(sender, wrapper.getPermission()) && matchesBeforeExecute(session, wrapper)) { //if access is allowed
                if (session.getArguments().isEmpty()) {
                    fireMentor(wrapper, session);
                    return;
                }

                fireProducer(wrapper, session, session.getArguments().getNativeArray());
            }
        }
    }

    private void fireProducer(WrappedCommand wrapper, CommandSession commandSession, String[] args) {
        final EntityCommandSender sender = commandSession.getSender();

        ProducerChild producerChild = wrapper.<ProducerChild>find(ProducerExecutor.class)
                .filter(producer -> producer.getName().equalsIgnoreCase(args[0]))
                .findFirst()
                .orElse(null);

        if (producerChild == null) {
            fireMentor(wrapper, commandSession);
            return;
        }

        String permission = producerChild.getPermission();

        if (permission == null || matchesPermission(sender, permission)) {
            CommandSession childSession = factory.createSession(wrapper.getHelpMessageView(), sender, factory.copyArgumentsOfRange(args));
            invokeMethod(childSession, wrapper.getSource(), producerChild.getMethod());
        }
    }

    private void fireMentor(WrappedCommand wrapper, CommandSession session) {
        MentorChild mentorChild = wrapper.<MentorChild>find(MentorExecutor.class)
                .findFirst()
                .orElse(null);

        if (mentorChild == null) {
            log.error(MENTOR_CHILD_NOT_FOUND_MESSAGE, wrapper.getName());
            return;
        }

        invokeMethod(session, wrapper.getSource(), mentorChild.getMethod());
    }

    private boolean matchesPermission(EntityCommandSender sender, String permission) {
        boolean hasPermission = sender.hasPermission(permission);

        if (!hasPermission)
            sender.sendMessage("§cYou do not have permission to execute this command");

        return hasPermission;
    }

    private boolean matchesBeforeExecute(CommandSession session, WrappedCommand wrapper) {
        long unauthorizedMatchers = wrapper.find(MatcherExecutor.class)
                .filter(matcher -> Boolean.FALSE.equals(invokeMethod(session, wrapper.getSource(), matcher.getMethod())))
                .count();

        return unauthorizedMatchers == 0;
    }

    private Object invokeMethod(CommandSession session, Object source, Method method) {
        return interceptor.callProxiedMethod(source, method, new Object[]{session});
    }
}
