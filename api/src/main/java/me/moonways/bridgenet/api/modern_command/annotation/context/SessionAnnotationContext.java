package me.moonways.bridgenet.api.modern_command.annotation.context;

import lombok.Getter;
import me.moonways.bridgenet.api.modern_command.data.CommandInfo;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;

import java.lang.annotation.Annotation;

@Getter
public class SessionAnnotationContext<T extends Annotation> extends AnnotationContext<T> {

    private final CommandSession session;

    public SessionAnnotationContext(T annotation, CommandInfo commandInfo, CommandSession session) {
        super(annotation, commandInfo);

        this.session = session;
    }
}
