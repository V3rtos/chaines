package me.moonways.bridgenet.test.engine.unit;

import me.moonways.bridgenet.test.engine.BridgenetBootstrapInitializer;

public interface TestRunnableStep {

    void process(BridgenetBootstrapInitializer initializer, TestUnit testUnit) throws Exception;
}
