package me.moonways.bridgenet.api.modern_x2_command.process.inject;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandInfo;

import java.lang.annotation.Annotation;

@Getter
@RequiredArgsConstructor
public class CommandBaseAnnotationContext<T extends Annotation> {

    private final T annotation;
    private final CommandInfo info;

    public static <T extends Annotation> CommandBaseAnnotationContext<T> create(T annotation, CommandInfo commandInfo) {
        return new CommandBaseAnnotationContext<>(annotation, commandInfo);
    }
}
