package me.moonways.bridgenet.api.modern_command.reflection;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.modern_command.*;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Description;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.EntityLevel;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.UsageCooldown;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Permission;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@UtilityClass
public class CommandReflectionUtil {

    public Aliases getAliasesAnnotation(Object object) {
        if (!isExistsAnnotation(object, Aliases.class)) {
            log.error("Annotation not found");
        }

        return object.getClass().getDeclaredAnnotation(Aliases.class);
    }

    public Aliases getAliasesAnnotation(Method method) {
        if (!isExistsAnnotation(method, Aliases.class)) {
            log.error("Annotation not found");
        }

        return method.getDeclaredAnnotation(Aliases.class);
    }

    public Permission getPermissionAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(Permission.class);
    }

    public EntityLevel getEntityLevelAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(EntityLevel.class);
    }

    public UsageCooldown getIntervalAnnotation(Object object) {
        return object.getClass().getDeclaredAnnotation(UsageCooldown.class);
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

    public UsageCooldown getIntervalAnnotation(Method method) {
        return method.getDeclaredAnnotation(UsageCooldown.class);
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
}
