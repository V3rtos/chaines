package me.moonways.bridgenet.test.engine.unit.step;

import me.moonways.bridgenet.test.engine.TestBridgenetBootstrap;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestUnit;
import org.junit.After;

public class TestFinishStep implements TestRunnableStep {

    @Override
    public void process(TestBridgenetBootstrap bootstrap, TestUnit testUnit) throws Exception {
        testUnit.invokeAnnotated(After.class);
    }
}
