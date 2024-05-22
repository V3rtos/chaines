package me.moonways.bridgenet.api.inject.processor;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationContext;
import me.moonways.bridgenet.api.inject.processor.verification.AnnotationVerificationResult;

import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;

@RequiredArgsConstructor
public abstract class TypeAnnotationProcessorAdapter<V extends Annotation> implements TypeAnnotationProcessor<V> {

    @SuppressWarnings("unchecked")
    public static <V> Class<V> getGenericType(int index, Class<?> root) {
        try {
            return (Class<V>) ((ParameterizedType) root.getGenericSuperclass()).getActualTypeArguments()[index];
        } catch (Throwable ex1) {
            try {
                return (Class<V>) ((ParameterizedType) root.getGenericInterfaces()[0]).getActualTypeArguments()[index];
            } catch (Throwable ex2) {
                return root.getGenericSuperclass().equals(Object.class) ? (Class<V>) Object.class : null;
            }
        }
    }

    protected Class<V> getAnnotationType() {
        return getGenericType(0, getClass());
    }

    @Override
    public AnnotationProcessorConfig<V> configure() {
        return AnnotationProcessorConfig.newConfigBuilder(getAnnotationType())
                .addPackage(System.getProperty("beans.package"))
                .build();
    }

    @Override
    public AnnotationVerificationResult verify(AnnotationVerificationContext<V> verification) {
        return verification.toResult().asSuccessful();
    }
}
