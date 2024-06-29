package me.moonways.bridgenet.test.engine;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.test.engine.flow.ParentFlowProcessor;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import me.moonways.bridgenet.test.engine.flow.TestFlowProcessor;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.TestClass;

import java.util.List;

@Log4j2
public class ModernTestEngineRunner extends BlockJUnit4ClassRunner {

    public ModernTestEngineRunner(TestClass testClass) throws InitializationError {
        super(testClass);
        log.debug("Testing class §b{} §rusing runner of §2TestEngine", testClass.getName());
    }

    public ModernTestEngineRunner(Class<?> testClass) throws InitializationError {
        this(new TestClass(testClass));
    }

    @Override
    public void run(RunNotifier notifier) {
        System.setProperty("test.engine.enabled", "true");

        TestFlowContext.sendEngineGreetingMessage();
        this.run(notifier, new TestingObject(getTestClass()));
    }

    public void run(RunNotifier notifier, TestingObject testingObject) {
        TestClass testClass = testingObject.getTestClass();

        TestFlowProcessor testFlowProcessor = new ParentFlowProcessor();
        List<TestFlowNode> flowNodes = testFlowProcessor.flowNodes();

        TestFlowContext flowContext = TestFlowContext.builder()
                .runner(this)
                .processor(testFlowProcessor)
                .flowNodes(flowNodes)
                .runNotifier(notifier)
                .testClass(testClass)
                .testingObject(testingObject)
                .build();

        runWithContext(flowContext);
    }

    public void runWithContext(TestFlowContext flowContext) {
        TestClass testClass = flowContext.getTestClass();
        List<TestFlowNode> flowNodes = flowContext.getFlowNodes();

        log.debug("Using test-processor context with §2{} steps §rfor §2{}", flowNodes.size(), testClass.getName());

        for (TestFlowNode flowNode : flowNodes) {
            executeFlowNode(testClass, flowNode, flowContext);
        }
    }

    private void executeFlowNode(TestClass testClass, TestFlowNode flowNode, TestFlowContext flowContext) {
        String simpleName = flowNode.getClass().getSimpleName();

        log.debug("Flow: §7{}.execute <-> begin; §r[for {}]", simpleName, testClass.getJavaClass());

        flowNode.execute(flowContext);

        log.debug("Flow: §7{}.execute <-> end; §r[for {}]", simpleName, testClass.getJavaClass());
    }
}
