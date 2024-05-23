package me.moonways.bridgenet.test.engine.flow.nodes;

import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;

public class FlowUnitRunningNode implements TestFlowNode {

    @Override
    public void execute(TestFlowContext context) {
        context.getTestingObject().execute(context);
    }
}
