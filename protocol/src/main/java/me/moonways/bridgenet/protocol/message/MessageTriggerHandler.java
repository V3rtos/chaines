package me.moonways.bridgenet.protocol.message;

import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.message.exception.MessageHandleException;
import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.Inject;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Component
@NoArgsConstructor
public final class MessageTriggerHandler {

    @Inject
    private DependencyInjection dependencyInjection;

    @PostFactoryMethod
    private void initializeHandlers() {
        dependencyInjection.findComponentsIntoBasePackage(MessageHandler.class);
    }

    public void fireTriggers(@NotNull Message message) {
        for (Object messageHandler : dependencyInjection.getContainer().getFoundComponents(MessageHandler.class)) {
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
