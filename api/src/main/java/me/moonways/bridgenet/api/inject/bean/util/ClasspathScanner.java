package me.moonways.bridgenet.api.inject.bean.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.experimental.UtilityClass;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
@UtilityClass
public class ClasspathScanner {

    public Set<Class<?>> findAllClassesUsingClassLoader() throws IOException {
        ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(ClasspathScanner.class.getClassLoader())
                .getAllClasses();

        return classInfos.stream()
                .map(classInfo -> {
                    try {
                        return classInfo.load();
                    } catch (Throwable ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}
