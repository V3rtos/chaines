package me.moonways.bridgenet.test.engine.component.module;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.util.List;

@Getter
@Builder
@ToString
public final class ModuleConfig {

    private final Class<? extends Annotation> processingAnnotation;
    private final List<String> packagesToScanning;
    private final List<Class<? extends Module>> dependencies;
}
