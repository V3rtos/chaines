package me.moonways.bridgenet.test.engine.unit.step;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.test.engine.TestBridgenetBootstrapInitializer;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestUnit;

@Log4j2
public class TestEmulateRunningStep implements TestRunnableStep {

    @Override
    public void process(TestBridgenetBootstrapInitializer initializer, TestUnit testUnit) throws Exception {
        log.info("TestEngine was running for §c{}", testUnit.getName());

        // emulation test running.
        Thread.sleep(2000);
    }
}
