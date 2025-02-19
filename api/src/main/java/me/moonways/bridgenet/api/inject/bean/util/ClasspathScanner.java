package me.moonways.bridgenet.api.inject.bean.util;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@SuppressWarnings("UnstableApiUsage")
@UtilityClass
public class ClasspathScanner {

    private @NotNull String repeat(String string, int count) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < count; i++) {
            result.append(string);
        }
        return result.toString();
    }

    public Set<Class<?>> findAllClassesUsingClassLoader() throws IOException {
        AtomicInteger loadedClassesCount = new AtomicInteger(0);

        ImmutableSet<ClassPath.ClassInfo> classInfos = ClassPath.from(ClasspathScanner.class.getClassLoader())
                .getAllClasses();

        int totalClasses = classInfos.size();
        Set<Class<?>> allClasses = classInfos
                .stream()
                .map(classInfo -> {
                    int count = loadedClassesCount.incrementAndGet();

                    String progressBar = String.format("[%-50s]", repeat("#", (int) (Math.min(1.0, (double)count / totalClasses) * 50))); //предотвращение ошибки index out of bounds
                    String progressPercent = String.format("%.2f%%", (double) count / totalClasses * 100);

                    String formattedProgress = String.format("Loading resources: %d/%d %s %s", count, totalClasses, progressBar, progressPercent);
                    System.out.print("\r" + formattedProgress);

                    try {
                        return classInfo.load();
                    } catch (Throwable ignored) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        System.out.println();
        return allClasses;
    }
}
