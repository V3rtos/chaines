package me.moonways.bridgenet.pipeline;

import me.moonways.bridgenet.message.BridgenetMessageHandler;
import me.moonways.bridgenet.message.MessageContainer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BridgenetMessageHandlerFactory {

    @NotNull
    BridgenetMessageHandler create(@NotNull MessageContainer messageContainer);
}
