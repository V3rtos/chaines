package me.moonways.bridgenet.mtp.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.Inject;
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

@NoArgsConstructor
public final class MessageHandlerList {

    @Getter
    private Set<HandlerMethodWrapper> messageHandlers;

    @Inject
    private DependencyInjection dependencyInjection;

    public void detectHandlers() {
        dependencyInjection.findComponentsIntoBasePackage(MessageHandler.class);
        messageHandlers = dependencyInjection.getContainer().getFoundComponents(MessageHandler.class)
                .stream()
                .flatMap(object -> Arrays.stream(object.getClass().getDeclaredMethods())
                        .map(method -> new HandlerMethodWrapper(object, object.getClass(), method)))
                .filter(method -> method.hasAnnotation(MessageTrigger.class))
                .collect(Collectors.toSet());
    }

    public void handle(@NotNull MessageWrapper message) {
        for (HandlerMethodWrapper wrapper : messageHandlers) {

            Method method = wrapper.getMethod();
            Class<?> messageClass = message.getClass();

            if (method.getParameterCount() != 1) {
                throw new MessageHandleException(
                        String.format("Can't handle message %s in handler %s", messageClass.getName(),
                                wrapper.getSourceClass().getName()));
            }

            Class<?> methodMessageClass = method.getParameterTypes()[0];
            if (messageClass.equals(methodMessageClass)) {
                try {
                    method.invoke(wrapper.getSource(), message);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class HandlerMethodWrapper {

        private final Object source;
        private final Class<?> sourceClass;

        private final Method method;

        public boolean hasAnnotation(Class<? extends Annotation> annotation) {
            return method.isAnnotationPresent(annotation);
        }
    }
}
