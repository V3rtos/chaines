package me.moonways.bridgenet.test.engine.unit.step;

import me.moonways.bridgenet.test.engine.BridgenetBootstrapInitializer;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestUnit;
import org.junit.Before;

public class TestCreateStep implements TestRunnableStep {

    @Override
    public void process(BridgenetBootstrapInitializer initializer, TestUnit testUnit) throws Exception {
        testUnit.invokeAnnotated(Before.class);
    }
}
