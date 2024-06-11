package me.moonways.bridgenet.test.engine.component.module;

import me.moonways.bridgenet.test.engine.flow.TestFlowContext;

public interface Module {

    ModuleConfig config();

    void install(TestFlowContext context);
}
