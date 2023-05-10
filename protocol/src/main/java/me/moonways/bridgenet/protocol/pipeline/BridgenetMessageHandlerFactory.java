package me.moonways.bridgenet.protocol.pipeline;

import me.moonways.bridgenet.protocol.message.BridgenetMessageHandlerProvider;
import me.moonways.bridgenet.protocol.message.MessageContainer;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface BridgenetMessageHandlerFactory {

    @NotNull
    BridgenetMessageHandlerProvider create(@NotNull MessageContainer messageContainer);
}
