package me.moonways.bridgenet.api.modern_command.reflect;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@UtilityClass
public class CommandReflectionUtil {

    public <T extends Annotation> T getAnnotation(Class<?> parentCls, Class<T> annotationCls) {
        return parentCls.getDeclaredAnnotation(annotationCls);
    }

    public <T extends Annotation> T getAnnotation(Method handle, Class<T> annotationCls) {
        return handle.getDeclaredAnnotation(annotationCls);
    }

    public List<Method> getMethodsBy(Class<?> parentCls, Class<? extends Annotation> cls) {
        List<Method> methods = new ArrayList<>();

        for (Method method : parentCls.getDeclaredMethods()) {
            if (!isExistsAnnotation(method, cls))
                continue;

            methods.add(method);
        }

        return methods;
    }

    public Method getMethodBy(Class<?> parentCls, Class<? extends Annotation> cls) {
        return getMethodsBy(parentCls, cls).stream().findFirst().orElse(null);
    }

    public boolean isExistsAnnotation(Class<?> parentCls, Class<? extends Annotation> annotationClass) {
        return parentCls.isAnnotationPresent(annotationClass);
    }

    public boolean isExistsAnnotation(Method method, Class<? extends Annotation> annotationClass) {
        return method.isAnnotationPresent(annotationClass);
    }
}
