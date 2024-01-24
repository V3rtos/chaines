package me.moonways.bridgenet.api.modern_command.annotation.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_command.data.CommandInfo;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
@Getter
public class AnnotationContext<T extends Annotation> {

    private final T annotation;
    private final CommandInfo commandInfo;
}
