package me.moonways.bridgenet.test.engine.flow.nodes;

import me.moonways.bridgenet.test.engine.TestingObject;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

public class FlowUnitExternalsPostExecutionNode extends FlowUnitExternalsExecutionNode {

    @Override
    public void execute(TestFlowContext context) {
        TestingObject testingObject = context.getTestingObject();

        testingObject.getExternalsUnits()
                .stream()
                .filter(externalTestingObject -> !canApplyHere(externalTestingObject))
                .forEach(externalTestingObject -> doExecute(context, externalTestingObject));
    }
}
