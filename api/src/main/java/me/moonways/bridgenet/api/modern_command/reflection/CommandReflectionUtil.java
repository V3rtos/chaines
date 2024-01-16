package me.moonways.bridgenet.api.modern_command.reflection;

import lombok.experimental.UtilityClass;
import me.moonways.bridgenet.api.modern_command.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class CommandReflectionUtil {

    public Aliases getAliasesAnnotation(Object object) {
        if (!isExistsAnnotation(object, Aliases.class)) {
            error("Annotation not found");
        }

        return object.getClass().getDeclaredAnnotation(Aliases.class);
    }

    public Aliases getAliasesAnnotation(Method method) {
        if (!isExistsAnnotation(method, Aliases.class)) {
            error("Annotation not found");
        }

        return method.getDeclaredAnnotation(Aliases.class);
    }

    public Permission getPermissionAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(Permission.class);
    }

    public EntityLevel getEntityLevelAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(EntityLevel.class);
    }

    public Interval getIntervalAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(Interval.class);
    }

    public Description getDescriptionAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(Description.class);
    }

    public Permission getPermissionAnnotation(Method method) {
        return method.getDeclaredAnnotation(Permission.class);
    }

    public EntityLevel getEntityLevelAnnotation(Method method) {
        return method.getDeclaredAnnotation(EntityLevel.class);
    }

    public Interval getIntervalAnnotation(Method method) {
        return method.getDeclaredAnnotation(Interval.class);
    }

    public Description getDescriptionAnnotation(Method method) {
        return method.getDeclaredAnnotation(Description.class);
    }

    public List<Method> getMethodsOfSubcommands(Object object) {
        List<Method> methods = new ArrayList<>();

        for (Method method : object.getClass().getDeclaredMethods()) {
            if (!isExistsAnnotation(method, Aliases.class))
                continue;

            methods.add(method);
        }

        return methods;
    }

    public boolean isExistsAnnotation(Object object, Class<? extends Annotation> annotationClass) {
        return object.getClass().isAnnotationPresent(annotationClass);
    }

    public boolean isExistsAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }

    private void error(String message) {
        throw new RuntimeException(message);
    }
}
