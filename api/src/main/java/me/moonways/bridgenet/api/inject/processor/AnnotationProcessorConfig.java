package me.moonways.bridgenet.api.inject.processor;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class AnnotationProcessorConfig<V extends Annotation> {

    public static <V extends Annotation> Builder<V> newConfigBuilder(Class<V> annotationType) {
        return new Builder<>(annotationType);
    }

    private final Class<V> annotationType;
    private final Set<String> packages;

    public Builder<V> toBuilder() {
        return new Builder<>(annotationType, packages);
    }

    @AllArgsConstructor
    @RequiredArgsConstructor
    public static final class Builder<V extends Annotation> {

        private final Class<V> annotationType;

        private Set<String> packages;

        public Builder<V> addPackage(String packageName) {
            if (packages == null) {
                packages = new HashSet<>();
            }
            packages.add(packageName);
            return this;
        }

        public Builder<V> addPackages(String... packageNames) {
            if (packages == null) {
                packages = new HashSet<>();
            }
            packages.addAll(Arrays.asList(packageNames));
            return this;
        }

        public AnnotationProcessorConfig<V> build() {
            return new AnnotationProcessorConfig<>(annotationType, packages);
        }
    }
}
