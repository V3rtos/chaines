package me.moonways.bridgenet.protocol.pipeline;

import me.moonways.bridgenet.protocol.message.BridgenetMessageHandler;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BridgenetMessageHandlerFactory {

    @NotNull
    BridgenetMessageHandler create(@NotNull MessageContainer messageContainer);
}
