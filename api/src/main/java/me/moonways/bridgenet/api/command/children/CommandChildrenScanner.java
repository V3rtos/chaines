package me.moonways.bridgenet.api.command.children;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.annotation.*;
import me.moonways.bridgenet.api.command.annotation.repeatable.RepeatableCommandAliases;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandChildrenScanner {

    private static final CommandChildrenFactory FACTORY = new CommandChildrenFactory();

    private Stream<Method> toStream(Object sourceObject, Class<? extends Annotation> annotationCls) {
        return Arrays.stream(sourceObject.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(annotationCls));
    }

    public CommandChild findMentorChild(@NotNull Object object) {
        Method method = toStream(object, MentorExecutor.class)
                .findFirst()
                .orElse(null);

        return FACTORY.createMentor(object, method);
    }

    public List<CommandChild> findPredicateChildren(@NotNull Object object) {
        return toStream(object, MatcherExecutor.class)
                .sorted(Comparator.comparing(method -> method.getDeclaredAnnotation(MatcherExecutor.class).priority()))
                .map(method -> FACTORY.createPredicate(object, method))
                .collect(Collectors.toList());
    }

    public List<CommandChild> findProducerChildren(@NotNull Object object) {
        return toStream(object, ProducerExecutor.class)
                .map(new MethodToProducerMapper(object))
                .collect(Collectors.toList());
    }

    @RequiredArgsConstructor
    private static class MethodToProducerMapper implements Function<Method, CommandChild> {

        private final Object source;

        private List<String> findAliases(Method method) {
            List<String> result = new ArrayList<>();

            RepeatableCommandAliases repeatableAliasesAnnotation = method.getDeclaredAnnotation(RepeatableCommandAliases.class);
            if (repeatableAliasesAnnotation != null) {
                result.addAll(
                        Arrays.stream(repeatableAliasesAnnotation.value())
                                .map(Alias::value)
                                .map(String::toLowerCase)
                                .collect(Collectors.toList()));
            } else if (method.isAnnotationPresent(Alias.class)) {
                result.add(method.getDeclaredAnnotation(Alias.class).value().toLowerCase());
            }

            return result;
        }

        private String findPermission(Method method) {
            Permission annotation = method.getDeclaredAnnotation(Permission.class);
            return annotation == null ? null : annotation.value();
        }

        private String findDescription(Method method) {
            ProducerDescription annotation = method.getDeclaredAnnotation(ProducerDescription.class);
            return annotation == null ? null : annotation.value();
        }

        private String findUsageDescription(Method method) {
            ProducerUsageDescription annotation = method.getDeclaredAnnotation(ProducerUsageDescription.class);
            return annotation == null ? null : annotation.value();
        }

        @Override
        public CommandChild apply(Method method) {
            ProducerExecutor producerAnnotation = method.getDeclaredAnnotation(ProducerExecutor.class);
            String producerName = producerAnnotation.value();

            return FACTORY.createProducer(source, method, producerName,
                    findAliases(method),
                    findPermission(method),
                    findUsageDescription(method),
                    findDescription(method));
        }
    }
}
