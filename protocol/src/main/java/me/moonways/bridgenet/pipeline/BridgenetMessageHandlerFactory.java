package me.moonways.bridgenet.pipeline;

import me.moonways.bridgenet.BridgenetMessageHandler;
import me.moonways.bridgenet.MessageRegistryContainer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BridgenetMessageHandlerFactory {

    @NotNull
    BridgenetMessageHandler create(@NotNull MessageRegistryContainer registryContainer);
}
