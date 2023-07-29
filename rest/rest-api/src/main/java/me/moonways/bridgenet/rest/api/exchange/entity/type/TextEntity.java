package me.moonways.bridgenet.rest.api.exchange.entity.type;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.api.exchange.entity.EntityWriter;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@RequiredArgsConstructor
public class TextEntity implements ExchangeableEntity {

    private final String text;

    @Override
    public void write(@NotNull EntityWriter entityWriter) {
        entityWriter.write(text);
    }

    @Override
    public byte[] toByteArray() {
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
