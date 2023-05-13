package me.moonways.bridgenet.protocol.message;

import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import me.moonways.bridgenet.protocol.message.exception.MessageHandleException;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.InitMethod;
import me.moonways.bridgenet.service.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@NoArgsConstructor
public final class MessageTriggerHandler {

    @Inject
    private DependencyInjection dependencyInjection;

    @InitMethod
    private void initializeHandlers() {
        dependencyInjection.scanDependenciesOfBasicPackage(MessageHandler.class);
    }

    public void fireTriggers(@NotNull Message message) {
        for (Object messageHandler : dependencyInjection.getInjectedDependsByAnnotation(MessageHandler.class)) {
            for (Method method : messageHandler.getClass().getDeclaredMethods()) {
                if (method.isAnnotationPresent(MessageTrigger.class)) {

                    Class<?> messageClass = message.getClass();

                    if (method.getParameterCount() != 1) {
                        throw new MessageHandleException(
                                String.format("Can't handle message %s in handler %s", messageClass.getName(),
                                        messageHandler.getClass().getName()));
                    }

                    Class<?> methodMessageClass = method.getParameterTypes()[0];

                    if (messageClass.isAssignableFrom(methodMessageClass)) {
                        try {
                            method.invoke(messageHandler, message);
                        } catch (IllegalAccessException | InvocationTargetException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
    }
}
