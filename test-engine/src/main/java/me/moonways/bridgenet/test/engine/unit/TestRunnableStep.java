package me.moonways.bridgenet.test.engine.unit;

import me.moonways.bridgenet.test.engine.TestBridgenetBootstrap;

public interface TestRunnableStep {

    void process(TestBridgenetBootstrap bootstrap, TestUnit testUnit) throws Exception;
}
