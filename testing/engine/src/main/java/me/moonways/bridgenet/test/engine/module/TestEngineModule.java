package me.moonways.bridgenet.test.engine.module;

import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

public interface TestEngineModule {

    TestModuleBeans getBeans();

    void onInstall(TestFlowContext context);
}
