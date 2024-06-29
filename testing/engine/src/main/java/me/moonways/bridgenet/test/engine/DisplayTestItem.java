package me.moonways.bridgenet.test.engine;

import lombok.Builder;
import me.moonways.bridgenet.test.data.junit.notification.EmptyTraceFailure;
import org.junit.runner.Description;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;

@Builder
public class DisplayTestItem {

    private final TestClass testClass;
    private final String displayName;
    private final RunNotifier notifier;

    private Description description;

    private Description createJunitDescription() {
        if (description == null) {
            description = Description.createTestDescription(testClass.getJavaClass(), displayName);
        }
        return description;
    }

    public void fireStarted() {
        notifier.fireTestStarted(createJunitDescription());
    }

    public void fireFinished() {
        notifier.fireTestFinished(createJunitDescription());
    }

    public void fireFinishedFailure() {
        notifier.fireTestFailure(new EmptyTraceFailure(createJunitDescription()));
    }
}
