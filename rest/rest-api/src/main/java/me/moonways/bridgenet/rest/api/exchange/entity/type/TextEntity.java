package me.moonways.bridgenet.rest.api.exchange.entity.type;

import lombok.*;
import me.moonways.bridgenet.rest.api.exchange.entity.EntityWriter;
import me.moonways.bridgenet.rest.api.exchange.entity.ExchangeableEntity;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;

@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TextEntity implements ExchangeableEntity {

    @Setter
    private String text;

    @Override
    public void setContent(Object object) {
        this.text = object.toString();
    }

    @Override
    public void write(@NotNull EntityWriter entityWriter) {
        entityWriter.write(text);
    }

    @Override
    public byte[] toByteArray() {
        return text.getBytes(StandardCharsets.UTF_8);
    }
}
