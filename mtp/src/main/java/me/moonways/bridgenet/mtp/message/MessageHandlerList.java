package me.moonways.bridgenet.mtp.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.persistence.MessageHandler;
import me.moonways.bridgenet.mtp.message.persistence.MessageTrigger;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class MessageHandlerList {

    @Getter
    private final Set<MethodTriggerState> messageHandlers = new HashSet<>();

    @Inject
    private DependencyInjection injector;

    public void bindHandlers() {
        injector.peekAnnotatedMembers(MessageHandler.class)
                .forEachOrdered(this::bind);
    }

    private List<MethodTriggerState> toStates(Object handler) {
        return Arrays.stream(handler.getClass().getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(MessageTrigger.class) != null)
                .map(method -> new MethodTriggerState(handler, handler.getClass(), method))
                .collect(Collectors.toList());
    }

    public void bind(Object handler) {
        if (messageHandlers.addAll(toStates(handler))) {

            log.info("Bind message handler: §6{}", handler.getClass().getSimpleName());

            injector.injectFields(handler);
        }
    }

    public void handle(@NotNull MessageWrapper wrapper, @NotNull InputMessageContext<?> context) {
        String messageName = context.getMessage().getClass().getSimpleName();

        int handlingCount = 0;
        for (MethodTriggerState triggerMethod : messageHandlers) {

            Method method = triggerMethod.getMethod();
            Class<?> messageClass = wrapper.getMessageType();

            if (method.getParameterCount() != 1) {
                throw new MessageHandleException(
                        String.format("Can't handle message %s in handler %s", messageClass.getName(),
                                triggerMethod.getSourceClass().getName()));
            }

            Class<?> methodMessageClass = method.getParameterTypes()[0];
            try {
                Object value;
                if (methodMessageClass.isAssignableFrom(messageClass)) {
                    value = context.getMessage();
                } else if (methodMessageClass.equals(InputMessageContext.class)) {
                    value = context;
                } else {
                    continue;
                }

                method.invoke(triggerMethod.getSource(), value);
                handlingCount++;

                log.info("Received message §3{} §rhandled in §2{}",
                        messageName,
                        triggerMethod.getSource().getClass().getSimpleName());
            }
            catch (IllegalAccessException | InvocationTargetException | ClassCastException exception) {
                if (!(exception instanceof ClassCastException)) {
                    throw new MessageHandleException(exception);
                }
            }
        }

        if (handlingCount == 0) {
            log.info("§4No one founded message handler for '{}'", messageName);
        }
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class MethodTriggerState {

        private final Object source;
        private final Class<?> sourceClass;

        private final Method method;
    }
}
