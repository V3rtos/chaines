package me.moonways.bridgenet.test.engine.unit.step;

import me.moonways.bridgenet.test.engine.TestBridgenetBootstrap;
import me.moonways.bridgenet.test.engine.unit.TestObjectUnit;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import org.junit.After;

public class TestFinishStep implements TestRunnableStep {

    @Override
    public void process(TestBridgenetBootstrap bootstrap, TestObjectUnit testUnit) throws Exception {
        testUnit.invokeAnnotated(After.class);
    }
}
