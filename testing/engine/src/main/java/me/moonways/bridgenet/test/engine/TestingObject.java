package me.moonways.bridgenet.test.engine;

import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.test.data.junit.notification.EmptyTraceFailure;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.persistance.AfterAll;
import me.moonways.bridgenet.test.engine.persistance.BeforeAll;
import me.moonways.bridgenet.test.engine.persistance.TestExternal;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.lang.annotation.Annotation;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

@ToString(onlyExplicitlyIncluded = true)
public class TestingObject extends TestingElement {

    @Getter
    @ToString.Include
    private final TestClass testClass;
    private final List<TestingElement> testingElementsList = new CopyOnWriteArrayList<>();

    private RunNotifier junitNotifier;

    @ToString.Include
    private Object instance;

    public TestingObject(TestClass testClass) {
        super(null);
        this.testClass = testClass;
    }

    @Override
    public String getName() {
        return testClass.getName();
    }

    @Override
    public void execute(TestFlowContext context) {
        fireAnnotatedWithBeforeAll(context);

        testingElementsList.stream()
                .sorted(Comparator.comparingInt(TestingElement::getElementOrder))
                .forEach(testingElement -> fireElement(context, testingElement));

        fireAnnotatedWithAfterAll(context);
    }

    public Object getInstance() {
        if (instance == null) {
            instance = BeanFactoryProviders.DEFAULT.getImpl()
                    .get()
                    .create(testClass.getJavaClass());
        }
        return instance;
    }

    public void init(RunNotifier runNotifier, List<TestingElement> elements) {
        this.junitNotifier = runNotifier;
        this.testingElementsList.addAll(elements);
    }

    private void fireElement(TestFlowContext context, TestingElement testingElement) {
        Description description = testingElement.createJunitDescription(testClass);

        junitNotifier.fireTestStarted(description);
        callElement(context, description, testingElement);
        junitNotifier.fireTestFinished(description);
    }

    private void callElement(TestFlowContext context, Description description, TestingElement testingElement) {
        long beforeSleepingDurationMs = testingElement.getBeforeSleepingDurationMs();
        long postSleepingDurationMs = testingElement.getPostSleepingDurationMs();

        try {
            if (beforeSleepingDurationMs > 0) {
                Thread.sleep(beforeSleepingDurationMs);
            }

            fireAnnotatedWithBefore(context);
            testingElement.execute(context);
            fireAnnotatedWithAfter(context);

            if (postSleepingDurationMs > 0) {
                Thread.sleep(postSleepingDurationMs);
            }
        }
        catch (Throwable exception) {
            handleException(context, exception);
            junitNotifier.fireTestFailure(new EmptyTraceFailure(description));
        }
    }

    private void handleException(TestFlowContext context, Throwable exception) {
        context.throwException(exception.getCause());
        TestFlowContext.sendWarnOfErrorMessage();
    }

    public void fireAnnotatedWithBefore(TestFlowContext context) {
        fireAnnotatedMethodWith(context, Before.class);
    }

    public void fireAnnotatedWithAfter(TestFlowContext context) {
        fireAnnotatedMethodWith(context, After.class);
    }

    public void fireAnnotatedWithBeforeAll(TestFlowContext context) {
        fireAnnotatedMethodWith(context, BeforeAll.class);
    }

    public void fireAnnotatedWithAfterAll(TestFlowContext context) {
        fireAnnotatedMethodWith(context, AfterAll.class);
    }

    private void fireAnnotatedMethodWith(TestFlowContext context, Class<? extends Annotation> annotationType) {
        List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods(annotationType);
        annotatedMethods.forEach(
                (frameworkMethod) -> {
                    try {
                        frameworkMethod.invokeExplosively(
                                frameworkMethod.isStatic() ? null : getInstance());
                    } catch (Throwable exception) {
                        handleException(context, exception);
                    }
                });
    }

    public List<ExternalTestingObject> getExternalsUnits() {
        List<FrameworkField> externalFields = testClass.getAnnotatedFields(TestExternal.class);
        return externalFields.stream()
                .map(ExternalTestingObject::new)
                .collect(Collectors.toList());
    }
}
