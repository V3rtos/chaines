package me.moonways.bridgenet.api.command.wrapper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.children.CommandChild;
import me.moonways.bridgenet.api.command.option.CommandParameterMatcher;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public final class WrappedCommand {

    private final String name;
    private final String permission;

    private final Object source;

    private final List<CommandChild> childrenList;

    private final List<CommandParameterMatcher> optionsList;

    private final CommandSession.HelpMessageView helpMessageView;

    @SuppressWarnings("unchecked")
    public <T extends CommandChild> Stream<T> find(@NotNull Class<? extends Annotation> annotationClass) {
        return childrenList
                .stream()
                .map(commandChild -> (T) commandChild)
                .filter(commandChild -> commandChild.getMethod().isAnnotationPresent(annotationClass));
    }
}
