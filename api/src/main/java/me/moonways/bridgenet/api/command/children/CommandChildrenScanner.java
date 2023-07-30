package me.moonways.bridgenet.api.command.children;

import me.moonways.bridgenet.api.command.children.definition.MentorChild;
import me.moonways.bridgenet.api.command.children.definition.PredicateChild;
import me.moonways.bridgenet.api.command.children.definition.ProducerChild;
import me.moonways.bridgenet.api.command.annotation.Mentor;
import me.moonways.bridgenet.api.command.annotation.Permission;
import me.moonways.bridgenet.api.command.annotation.Matcher;
import me.moonways.bridgenet.api.command.annotation.Producer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandChildrenScanner {

    @SuppressWarnings("DataFlowIssue")
    public CommandChild findMentorChild(@NotNull Object object) {
        Method method = findChildren(object, Mentor.class)
                .findFirst().orElse(null);
        return createMentor(object, method);
    }

    public List<CommandChild> findPredicateChildren(@NotNull Object object) {
        return findChildren(object, Matcher.class)
                .sorted(Comparator.comparing(method -> method.getDeclaredAnnotation(Matcher.class).priority()))
                .map(method -> createPredicate(object, method))
                .collect(Collectors.toList());
    }

    public List<CommandChild> findProducerChildren(@NotNull Object object) {
        return findChildren(object, Producer.class).map(method -> {
            Producer producer = method.getDeclaredAnnotation(Producer.class);
            Permission permission = method.getDeclaredAnnotation(Permission.class);

            return createProducer(
                    object,
                    method,
                    producer.name(),
                    permission == null ? null : permission.value());
        }).collect(Collectors.toList());
    }

    private Stream<Method> findChildren(@NotNull Object sourceObject,
                                        @NotNull Class<? extends Annotation> annotationCls) {
        return Arrays
                .stream(sourceObject.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationCls));
    }

    private CommandChild createMentor(@NotNull Object sourceObject, @NotNull Method method) {
        return new MentorChild(sourceObject, method);
    }

    private CommandChild createProducer(@NotNull Object sourceObject,
                                        @NotNull Method method,
                                        @Nullable String name,
                                        @Nullable String permission) {
        return new ProducerChild(sourceObject, method, name, permission);
    }

    private CommandChild createPredicate(@NotNull Object sourceObject, @NotNull Method method) {
        return new PredicateChild(sourceObject, method);
    }
}
