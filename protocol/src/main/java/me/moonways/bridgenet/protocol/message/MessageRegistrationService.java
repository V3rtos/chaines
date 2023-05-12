package me.moonways.bridgenet.protocol.message;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.protocol.exception.MessageNotFoundException;
import me.moonways.bridgenet.protocol.message.exception.MessageRegisterException;
import me.moonways.bridgenet.service.inject.Component;
import me.moonways.bridgenet.service.inject.DependencyInjection;
import me.moonways.bridgenet.service.inject.Inject;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
@Log4j2
@Component
public class MessageRegistrationService {

    private final Map<Integer, Class<?>> messageIdentifierMap = new HashMap<>();

    @Inject
    private DependencyInjection dependencyInjection;

    public void registerAll(@NotNull ProtocolDirection protocolDirection) {
        int counter = 1;

        for (Object message : dependencyInjection.getInjectedDependsByAnnotation(MessageComponent.class)) {
            Class<?> messageClass = message.getClass();

            MessageComponent messageComponent = messageClass.getDeclaredAnnotation(MessageComponent.class);

            if (messageComponent == null) {
                continue;
            }

            ProtocolDirection messageProtocolDirection = messageComponent.direction();

            if (messageProtocolDirection == null) {
                throw new MessageRegisterException(String.format("Protocol direction is null in message %s",
                        messageClass));
            }

            if (protocolDirection.equals(messageProtocolDirection)) {
                if (Message.class.isAssignableFrom(messageClass)) {
                    addMessage(counter, messageClass);

                } else {
                    throw new MessageRegisterException(String.format("Can't register message %s because not message",
                            messageClass));
                }

                counter++;
            }
        }
    }

    public void addMessage(int id, Class<?> messageClass) {
        messageIdentifierMap.put(id, messageClass);

        log.info(String.format("Registered message: %s [id:%s]", messageClass, id));
    }

    public Class<?> getMessageById(int id) {
        return Optional.ofNullable(messageIdentifierMap.get(id))
                .orElseThrow(() -> new MessageNotFoundException(
                        String.format("Can't find registered message by id %s in container", id)));
    }

    public int getIdByMessage(Class<? extends Message> messageClass) {
        Map.Entry<Integer, Class<?>> integerClassEntry = messageIdentifierMap.entrySet().stream()
                .filter(entry -> entry.getValue().equals(messageClass))
                .findFirst().orElseThrow(() -> new NullPointerException("message"));

        return integerClassEntry.getKey();
    }
}
