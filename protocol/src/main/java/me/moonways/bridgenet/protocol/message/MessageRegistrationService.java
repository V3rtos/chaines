package me.moonways.bridgenet.protocol.message;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.protocol.message.exception.MessageRegisterException;
import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.DependencyInjection;
import me.moonways.bridgenet.injection.PostFactoryMethod;
import me.moonways.bridgenet.injection.Inject;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

@Log4j2
@Component
public class MessageRegistrationService {

    public static final int UNDEFINED_MESSAGE_CLASS_ID = -1;

    private final Map<Integer, Class<?>> messageByIdsMap = new HashMap<>();

    @Inject
    private DependencyInjection dependencyInjection;

    @PostFactoryMethod
    private void initializeMessages() {
        dependencyInjection.findComponentsIntoBasePackage(MessageComponent.class);
    }

    private void validateNull(Class<?> messageCls) {
        if (messageCls == null) {
            throw new NullPointerException("message class type");
        }
    }

    public void registerAll(@NotNull ProtocolDirection protocolDirection) {
        int counter = 1;

        for (Object message : dependencyInjection.getContainer().getFoundComponents(MessageComponent.class)) {
            Class<?> messageClass = message.getClass();

            MessageComponent messageComponent = messageClass.getDeclaredAnnotation(MessageComponent.class);
            if (messageComponent == null) {
                continue;
            }

            ProtocolDirection direction = messageComponent.direction();

            if (direction == null) {
                throw new MessageRegisterException(String.format("Protocol direction is null in message %s",
                        messageClass));
            }

            if (protocolDirection == direction) {
                if (Message.class.isAssignableFrom(messageClass)) {
                    registerMessage(counter, messageClass);

                } else {
                    throw new MessageRegisterException(String.format("Can't register message %s because not message",
                            messageClass));
                }

                counter++;
            }
        }
    }

    public void registerMessage(int id, @NotNull Class<?> messageClass) {
        log.printf(Level.INFO, "Registering protocol Message(ID: %d, Class: %s)", id, messageClass.getName());

        validateNull(messageClass);
        messageByIdsMap.put(id, messageClass);
    }

    public Class<?> getMessageClassById(int id) {
        Class<?> messageType = messageByIdsMap.get(id);
        validateNull(messageType);

        return messageType;
    }

    public int getIdByMessageClass(@NotNull Class<? extends Message> messageClass) {
        validateNull(messageClass);
        return messageByIdsMap
                .keySet()
                .stream()
                .filter(id -> messageByIdsMap.get(id) == messageClass)
                .findFirst()
                .orElse(UNDEFINED_MESSAGE_CLASS_ID);
    }
}
