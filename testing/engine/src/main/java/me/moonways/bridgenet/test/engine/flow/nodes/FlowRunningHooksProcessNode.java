package me.moonways.bridgenet.test.engine.flow.nodes;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookPriority;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;
import me.moonways.bridgenet.test.engine.flow.TestFlowNode;

@Log4j2
public class FlowRunningHooksProcessNode implements TestFlowNode {

    @Override
    public void execute(TestFlowContext context) {
        context.getInstance(TestFlowContext.BOOTSTRAP)
                .ifPresent(this::onPresent);
    }

    private void onPresent(AppBootstrap bootstrap) {
        log.debug("Processing bootstrap hooks of Bridgenet running...");

        bootstrap.processBootstrapHooks(BootstrapHookPriority.RUNNER);
        bootstrap.processBootstrapHooks(BootstrapHookPriority.POST_RUNNER);
    }
}
