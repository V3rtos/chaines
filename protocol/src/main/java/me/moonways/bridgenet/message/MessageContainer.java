package me.moonways.bridgenet.message;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class MessageContainer {

    public static MessageContainer createContainers() {
        final MessageResponseContainer responseContainer = new MessageResponseContainer();
        final MessageRegistry registryContainer = new MessageRegistry();

        return new MessageContainer(registryContainer, responseContainer);
    }

    @Delegate
    private final MessageRegistry registryContainer;

    @Delegate
    private final MessageResponseContainer responseContainer;
}
