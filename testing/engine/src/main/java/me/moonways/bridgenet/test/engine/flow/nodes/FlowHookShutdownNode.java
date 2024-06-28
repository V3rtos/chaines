package me.moonways.bridgenet.test.engine.flow.nodes;

import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;

public class FlowHookShutdownNode implements TestFlowNode {

    @Override
    public void execute(TestFlowContext context) {
        AppBootstrap bootstrap = context.getInstance(TestFlowContext.BOOTSTRAP).get();

        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(bootstrap::shutdownApp));
    }
}
