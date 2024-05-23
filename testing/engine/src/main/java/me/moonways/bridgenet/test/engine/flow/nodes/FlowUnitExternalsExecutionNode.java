package me.moonways.bridgenet.test.engine.flow.nodes;

import me.moonways.bridgenet.test.engine.ExternalTestingObject;
import me.moonways.bridgenet.test.engine.TestingObject;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;
import me.moonways.bridgenet.test.engine.persistance.PersistenceAcceptType;

public class FlowUnitExternalsExecutionNode implements TestFlowNode {

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

        Runnable submit = () -> context.getRunner().run(context.getRunNotifier(), testingObject);

        if (testingObject.getPersistenceAcceptType() == PersistenceAcceptType.PARALLEL) {
            context.getForkJoinPool().submit(submit);
            return;
        }

        submit.run();
    }

    protected static boolean canApplyHere(ExternalTestingObject obj) {
        return obj.getPersistenceAcceptType() == PersistenceAcceptType.BEFORE_EXECUTION
                || obj.getPersistenceAcceptType() == PersistenceAcceptType.PARALLEL;
    }
}
