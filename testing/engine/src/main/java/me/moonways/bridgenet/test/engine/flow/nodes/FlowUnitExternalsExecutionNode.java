package me.moonways.bridgenet.test.engine.flow.nodes;

import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.test.engine.ExternalTestingObject;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.TestingObject;
import me.moonways.bridgenet.test.engine.flow.ExternalFlowProcessor;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import me.moonways.bridgenet.test.engine.flow.TestFlowProcessor;
import me.moonways.bridgenet.test.engine.persistance.ExternalAcceptationType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FlowUnitExternalsExecutionNode implements TestFlowNode {
    private static final ExecutorService parallelism = Executors.newWorkStealingPool();

    @Override
    public void execute(TestFlowContext context) {
        TestingObject testingObject = context.getTestingObject();

        testingObject.getExternalsUnits()
                .stream()
                .filter(FlowUnitExternalsExecutionNode::canApplyHere)
                .forEach(externalTestingObject -> doExecute(context, externalTestingObject));
    }

    protected void doExecute(TestFlowContext context, ExternalTestingObject testingObject) {
        testingObject.setInstanceAt(context.getTestingObject().getInstance());

        Runnable submit = () -> {
            TestFlowProcessor testFlowProcessor = new ExternalFlowProcessor();
            ModernTestEngineRunner modernTestEngineRunner = context.getRunner();

            TestFlowContext externalFlowContext =
                    TestFlowContext.builder()
                            .runner(modernTestEngineRunner)
                            .processor(testFlowProcessor)
                            .flowNodes(testFlowProcessor.flowNodes())
                            .testingObject(testingObject)
                            .testClass(testingObject.getTestClass())
                            .runNotifier(context.getRunNotifier())
                            .build();

            BeansService beansService = context.getInstance(TestFlowContext.BEANS).orElse(null);

            externalFlowContext.setInstance(TestFlowContext.BOOTSTRAP, context.getInstance(TestFlowContext.BOOTSTRAP).orElse(null));
            externalFlowContext.setInstance(TestFlowContext.BEANS, beansService);
            externalFlowContext.setInstance(TestFlowContext.LOADED_MODULES, context.getInstance(TestFlowContext.LOADED_MODULES).orElse(null));

            if (beansService != null) {
                beansService.fakeBind(testingObject);
                beansService.fakeBind(testingObject.getInstance());
            }

            modernTestEngineRunner.runWithContext(externalFlowContext);
        };

        if (testingObject.getPersistenceAcceptType() == ExternalAcceptationType.PARALLEL) {
            parallelism.submit(submit);
            return;
        }

        submit.run();
    }

    protected static boolean canApplyHere(ExternalTestingObject obj) {
        return obj.getPersistenceAcceptType() == ExternalAcceptationType.BEFORE_UNIT
                || obj.getPersistenceAcceptType() == ExternalAcceptationType.PARALLEL;
    }
}
