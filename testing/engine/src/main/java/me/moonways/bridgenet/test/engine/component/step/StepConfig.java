package me.moonways.bridgenet.test.engine.component.step;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.test.engine.component.module.Module;

import java.util.List;

@Getter
@Builder
@ToString
public final class StepConfig {

    private final List<Class<? extends Step>> beforeSteps;
    private final List<Class<? extends Step>> afterSteps;

    private final List<Class<? extends Module>> modulesDependencies;

    private final List<Class<?>> beansDependencies;
}
