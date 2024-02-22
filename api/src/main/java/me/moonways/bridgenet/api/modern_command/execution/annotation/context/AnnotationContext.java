package me.moonways.bridgenet.api.modern_command.execution.annotation.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_command.Command;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
@Getter
public class AnnotationContext<T extends Annotation> {

    private final T annotation;
    private final Command command;
}
