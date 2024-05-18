package me.moonways.bridgenet.test.engine.module;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.lang.annotation.Annotation;
import java.util.List;

@Getter
@Builder
@ToString
public class TestModuleBeans {

    private final Class<? extends Annotation> processingAnnotation;
    private final List<String> packagesToScanning;
    private final List<Class<? extends TestEngineModule>> dependencies;
}
