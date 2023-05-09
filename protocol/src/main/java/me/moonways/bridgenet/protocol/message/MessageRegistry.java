package me.moonways.bridgenet.protocol.message;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.moonways.bridgenet.protocol.exception.MessageNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unused")
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class MessageRegistry {

    private final Map<Integer, Class<? extends Message>> messageIdentifierMap = new HashMap<>();

    public void registerMessage(int id, Class<? extends Message> messageClass) {
        messageIdentifierMap.put(id, messageClass);
    }

    public Class<? extends Message> getMessageById(int id) {
        return Optional.ofNullable(messageIdentifierMap.get(id))
                .orElseThrow(() -> new MessageNotFoundException(
                        String.format("Cannot find registered message by id %s in container", id)));
    }

    public int getIdByMessage(Class<? extends Message> messageClass) {
        return 0; // FIXME: 04.05.2023
    }
}
