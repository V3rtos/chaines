package me.moonways.bridgenet.mtp.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactory;
import me.moonways.bridgenet.api.inject.bean.factory.UnsafeFactory;
import me.moonways.bridgenet.mtp.ProtocolDirection;
import me.moonways.bridgenet.mtp.message.encryption.EncryptedMessage;

@Getter
@ToString(includeFieldNames = false)
@EqualsAndHashCode
@RequiredArgsConstructor
public class MessageWrapper {

    private static final BeanFactory FACTORY = new UnsafeFactory();

    @ToString.Include
    private final int id;

    @ToString.Include
    private final Class<?> messageType;

    @ToString.Include
    private final ProtocolDirection direction;

    public boolean isServerSide() {
        return direction == ProtocolDirection.TO_SERVER;
    }

    public boolean needsEncryption() {
        return messageType.isAnnotationPresent(EncryptedMessage.class);
    }

    public boolean matchesSimilar(MessageWrapper wrapper) {
        return wrapper.id == id;
    }

    public boolean matchesSimilar(Class<?> messageClass) {
        return messageType.equals(messageClass);
    }

    public synchronized Object allocate() {
        return FACTORY.create(messageType);
    }
}
