package me.moonways.bridgenet.rest.api.exchange.entity;

import org.jetbrains.annotations.NotNull;

public interface ExchangeableEntity {

    void setContent(Object object);

    void write(@NotNull EntityWriter entityWriter);

    byte[] toByteArray();
}
