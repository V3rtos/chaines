package me.moonways.bridgenet.test.engine.flow.nodes;

import me.moonways.bridgenet.test.engine.TestingElement;
import me.moonways.bridgenet.test.engine.TestingObject;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import org.junit.Test;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.TestClass;

import java.util.List;
import java.util.stream.Collectors;

public class FlowUnitPreparingNode implements TestFlowNode {

    @Override
    public void execute(TestFlowContext context) {
        TestClass testClass = context.getTestClass();
        TestingObject testingObject = context.getTestingObject();

        prepareTests(context, testClass, testingObject);
    }

    private void prepareTests(TestFlowContext context, TestClass testClass, TestingObject testingObject) {
        List<FrameworkMethod> annotatedMethods = testClass.getAnnotatedMethods();
        testingObject.init(context.getRunNotifier(),
                annotatedMethods.stream()
                        .filter(frameworkMethod -> frameworkMethod.getAnnotation(Test.class) != null)
                        .map(TestingElement::new)
                        .collect(Collectors.toList()));
    }
}
