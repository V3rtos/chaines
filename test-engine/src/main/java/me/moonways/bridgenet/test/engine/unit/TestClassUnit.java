package me.moonways.bridgenet.test.engine.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.test.engine.persistance.PutTestUnit;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Log4j2
@RequiredArgsConstructor
public class TestClassUnit {

    private final RunNotifier notifier;
    private final Object source;

    public String getName() {
        return source.getClass().getSimpleName();
    }

    public void invokeAnnotated(Class<? extends Annotation> annotation) throws Exception {
        for (Method method : getOrderedMethods()) {

            if (method.isAnnotationPresent(annotation)) {
                method.invoke(source);
            }
        }
    }

    public void peekAnnotated(Class<? extends Annotation> annotation, Consumer<Method> handler) throws Exception {
        for (Method method : getOrderedMethods()) {

            if (method.isAnnotationPresent(annotation)) {
                handler.accept(method);
            }
        }
    }

    public void invoke(String testName) throws Exception {
        Method method = source.getClass().getDeclaredMethod(testName);

        method.setAccessible(true);
        method.invoke(source);
    }

    private Method[] getOrderedMethods() {
        List<Method> methods = new ArrayList<>(Arrays.asList(source.getClass().getDeclaredMethods()));
        Map<Integer, Integer> orderedIndexesMap = new HashMap<>();

        int size = methods.size();
        for (int index = 0; index < size; index++) {
            Method method = methods.get(index);

            if (method.isAnnotationPresent(Order.class)) {
                int order = method.getDeclaredAnnotation(Order.class).value();
                if (order < 0) {
                    continue;
                }

                orderedIndexesMap.put(index, order);
            }
        }

        for (Map.Entry<Integer, Integer> indexWithOrderEntry : orderedIndexesMap.entrySet()) {
            int index = indexWithOrderEntry.getKey();
            int order = indexWithOrderEntry.getValue();

            Method method = methods.remove(index);
            methods.add(order, method);
        }

        return methods.toArray(new Method[0]);
    }

    public Set<ExternalUnit> getDelegateExternalsUnits() {
        return Stream.of(source.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(PutTestUnit.class))
                .map(field -> ExternalUnit.builder()
                        .instance(source)
                        .testClass(field.getType())
                        .factoryProvider(field.getDeclaredAnnotation(PutTestUnit.class).factory())
                        .field(field)
                        .build())
                .collect(Collectors.toSet());
    }
}
