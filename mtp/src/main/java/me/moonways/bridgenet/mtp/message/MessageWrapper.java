package me.moonways.bridgenet.mtp.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.injection.factory.ObjectFactory;
import me.moonways.bridgenet.injection.factory.UnsafeObjectFactory;
import me.moonways.bridgenet.mtp.ProtocolDirection;

@Getter
@ToString(includeFieldNames = false)
@EqualsAndHashCode
@RequiredArgsConstructor
public class MessageWrapper {

    private static final ObjectFactory FACTORY = new UnsafeObjectFactory();

    @ToString.Include
    private final int id;

    @ToString.Include
    private final Class<?> messageType;

    @ToString.Include
    private final ProtocolDirection direction;

    public boolean isServerSide() {
        return direction == ProtocolDirection.TO_SERVER;
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
