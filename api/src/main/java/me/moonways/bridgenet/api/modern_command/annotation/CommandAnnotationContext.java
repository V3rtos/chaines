package me.moonways.bridgenet.api.modern_command.annotation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.StandardCommandInfo;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;

import java.lang.annotation.Annotation;
import java.util.function.Consumer;

@Getter
@ToString
@RequiredArgsConstructor
public class CommandAnnotationContext<T extends Annotation> {

    private final T annotation;

    private final CommandSession session;
    private final StandardCommandInfo commandInfo;

    public <V extends StandardCommandInfo> void update(Class<V> type, Consumer<V> consumer) {
        try {
            @SuppressWarnings("unchecked") V cast = ((V) commandInfo);
            consumer.accept(cast);

        } catch (ClassCastException ignored) {
        }
    }

    public void update(Consumer<StandardCommandInfo> consumer) {
        consumer.accept(commandInfo);
    }
}
