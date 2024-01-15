package me.moonways.bridgenet.test.engine.unit;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.junit.runner.notification.RunNotifier;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class TestUnit {

    private final RunNotifier notifier;
    private final Object source;

    public String getName() {
        return source.getClass().getSimpleName();
    }

    public void invokeAnnotated(Class<? extends Annotation> annotation) throws Exception {
        for (Method method : source.getClass().getMethods()) {

            if (method.isAnnotationPresent(annotation)) {
                method.invoke(source);
            }
        }
    }

    public void peekAnnotated(Class<? extends Annotation> annotation, Consumer<Method> handler) throws Exception {
        for (Method method : source.getClass().getMethods()) {

            if (method.isAnnotationPresent(annotation)) {
                handler.accept(method);
            }
        }
    }

    public void invoke(String testName) throws Exception {
        Method method = source.getClass().getMethod(testName);

        method.setAccessible(true);
        method.invoke(source);
    }
}
