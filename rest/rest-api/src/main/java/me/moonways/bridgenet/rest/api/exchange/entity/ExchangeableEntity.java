package me.moonways.bridgenet.rest.api.exchange.entity;

import org.jetbrains.annotations.NotNull;

public interface ExchangeableEntity {

    void write(@NotNull EntityWriter entityWriter);

    byte[] toByteArray();
}
