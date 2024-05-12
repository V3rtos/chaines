package me.moonways.bridgenet.test.engine.unit.step;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.test.engine.TestBridgenetBootstrap;
import me.moonways.bridgenet.test.engine.unit.TestRunnableStep;
import me.moonways.bridgenet.test.engine.unit.TestObjectUnit;

@Log4j2
public class TestEmulateRunningStep implements TestRunnableStep {

    @Override
    public void process(TestBridgenetBootstrap bootstrap, TestObjectUnit testUnit) throws Exception {
        log.info("TestEngine was running for §c{}", testUnit.getName());

        // emulation test running.
        Thread.sleep(2000);
    }
}
