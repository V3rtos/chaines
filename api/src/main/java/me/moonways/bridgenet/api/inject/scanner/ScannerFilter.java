package me.moonways.bridgenet.api.inject.scanner;

import lombok.*;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

@Getter
@ToString
@Value(staticConstructor = "create")
public class ScannerFilter {

    Set<Class<? extends Annotation>> annotations = new HashSet<>();

    Set<String> packageNames = new HashSet<>();

    public ScannerFilter with(Class<? extends Annotation> annotationType) {
        annotations.add(annotationType);
        return this;
    }

    public ScannerFilter with(String packageName) {
        packageNames.add(packageName);
        return this;
    }
}
