package me.moonways.bridgenet.test.engine.component.step;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public abstract class StepAdapter implements Step {

    private final StepConfig config;

    @Override
    public void execute(TestFlowContext context) {
        // override me.
    }

    @Override
    public final StepConfig config() {
        return config;
    }
}
