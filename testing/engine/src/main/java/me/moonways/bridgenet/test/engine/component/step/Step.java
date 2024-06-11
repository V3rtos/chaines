package me.moonways.bridgenet.test.engine.component.step;

import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

public interface Step {

    StepConfig config();

    void execute(TestFlowContext context);
}
