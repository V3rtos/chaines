package me.moonways.bridgenet.model.commands;

import lombok.*;
import me.moonways.bridgenet.api.event.EventService;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Supplier;

@Getter
@ToString
@EqualsAndHashCode
@Builder(toBuilder = true)
public class CommandResult {

    public enum Type {
        CONFIRMED,
        FAILED,
    }

    private final Type type;
    private final Supplier<RuntimeException> failThrowableSuppler;
    private final CommandProcessEvent event;

    public void complete(@NotNull EventService eventService) {
        if (Objects.equals(type, Type.FAILED)) {
            throw Optional.ofNullable(failThrowableSuppler)
                    .map(Supplier::get)
                    .orElseGet(() -> new RuntimeException("Command session executed with FAIL"));
        }

        eventService.fireEvent(event);
    }
}
