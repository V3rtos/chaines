package me.moonways.bridgenet.test.engine.unit;

import me.moonways.bridgenet.test.engine.TestBridgenetBootstrapInitializer;

public interface TestRunnableStep {

    void process(TestBridgenetBootstrapInitializer initializer, TestUnit testUnit) throws Exception;
}
