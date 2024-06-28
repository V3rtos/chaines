package me.moonways.bridgenet.test.engine;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.util.reflection.ReflectionUtils;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.persistance.ExternalAcceptationType;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import me.moonways.bridgenet.test.engine.persistance.TestSleeping;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.Optional;

@Getter
@ToString
@RequiredArgsConstructor
public class TestingElement {

    private final FrameworkMethod frameworkMethod;
    private DisplayTestItem displayTestItem;

    public String getName() {
        return frameworkMethod.getName();
    }

    public final DisplayTestItem createDisplayTestItem(TestClass testClass, RunNotifier notifier) {
        if (displayTestItem == null) {
            displayTestItem = DisplayTestItem.builder()
                    .testClass(testClass)
                    .displayName(frameworkMethod.getName())
                    .notifier(notifier)
                    .build();
        }
        return displayTestItem;
    }

    public void execute(TestFlowContext context) {
        TestingObject testingObject = context.getTestingObject();
        ReflectionUtils.invoke(testingObject.getInstance(),
                frameworkMethod.getName());
    }

    public final int getElementOrder() {
        return Optional.ofNullable(frameworkMethod.getAnnotation(TestOrdered.class))
                .map(TestOrdered::value).orElse(0);
    }

    public final long getBeforeSleepingDurationMs() {
        return Optional.ofNullable(frameworkMethod.getAnnotation(TestSleeping.class))
                .filter(testSleeping -> testSleeping.acceptType() != ExternalAcceptationType.POST_UNIT)
                .map(TestSleeping::value).orElse(0);
    }

    public final long getPostSleepingDurationMs() {
        return Optional.ofNullable(frameworkMethod.getAnnotation(TestSleeping.class))
                .filter(testSleeping -> testSleeping.acceptType() == ExternalAcceptationType.POST_UNIT)
                .map(TestSleeping::value).orElse(0);
    }
}
