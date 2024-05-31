package me.moonways.bridgenet.test.engine.flow;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.test.engine.flow.nodes.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class ParentFlowProcessor implements TestFlowProcessor {

    private static final TestFlowNode[] ORDERED_NODES =
            {
                    new FlowSystemBootstrapNode(),
                    new FlowModulesApplyingNode(),
                    new FlowContinuesHooksProcessingNode(),
                    new FlowUnitPreparingNode(),
                    new FlowUnitExternalsExecutionNode(),
                    new FlowUnitRunningNode(),
                    new FlowUnitExternalsPostExecutionNode(),
                    new FlowHookShutdownNode(),
            };

    @Override
    public List<TestFlowNode> flowNodes() {
        return Collections.unmodifiableList(Arrays.asList(ORDERED_NODES));
    }
}
