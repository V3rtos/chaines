package me.moonways.bridgenet.mtp.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.injection.proxy.ProxiedKeepTimeMethod;
import me.moonways.bridgenet.mtp.message.exception.MessageHandleException;
import me.moonways.bridgenet.mtp.message.inject.MessageHandler;
import me.moonways.bridgenet.mtp.message.inject.MessageTrigger;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
public class MessageHandlerList {

    @Getter
    private Set<TriggerMethodWrapper> messageHandlers;

    @Inject
    private DependencyInjection dependencyInjection;

    public void detectHandlers() {
        dependencyInjection.findComponentsIntoBasePackage(MessageHandler.class);
        messageHandlers = dependencyInjection.getContainer().getFoundComponents(MessageHandler.class)
                .stream()
                .flatMap(object -> Arrays.stream(object.getClass().getDeclaredMethods())
                        .map(method -> new TriggerMethodWrapper(object, object.getClass(), method)))
                .filter(method -> method.hasAnnotation(MessageTrigger.class))
                .collect(Collectors.toSet());
    }

    @ProxiedKeepTimeMethod
    public void handle(@NotNull MessageWrapper wrapper, @NotNull Object message) {
        for (TriggerMethodWrapper triggerMethod : messageHandlers) {

            Method method = triggerMethod.getMethod();
            Class<?> messageClass = wrapper.getMessageType();

            if (method.getParameterCount() != 1) {
                throw new MessageHandleException(
                        String.format("Can't handle message %s in handler %s", messageClass.getName(),
                                triggerMethod.getSourceClass().getName()));
            }

            Class<?> methodMessageClass = method.getParameterTypes()[0];
            if (messageClass.equals(methodMessageClass)) {
                try {
                    method.invoke(triggerMethod.getSource(), message);
                } catch (IllegalAccessException | InvocationTargetException exception) {
                    log.error(exception);
                }
            }
        }
    }

    @Getter
    @ToString
    @RequiredArgsConstructor
    public static class TriggerMethodWrapper {

        private final Object source;
        private final Class<?> sourceClass;

        private final Method method;

        public boolean hasAnnotation(Class<? extends Annotation> annotation) {
            return method.isAnnotationPresent(annotation);
        }
    }
}
