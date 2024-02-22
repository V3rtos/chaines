package me.moonways.bridgenet.api.modern_x2_command.install.reflect;

import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ReflectionUtil {

    public List<Method> find(Class<?> parentCls, @NotNull Class<? extends Annotation> cls) {
        List<Method> methods = new ArrayList<>();

        for (Method method : parentCls.getDeclaredMethods()) {
            if (method.isAnnotationPresent(cls)) {
                methods.add(method);
            }
        }

        return methods;
    }

    public <T extends Annotation> T get(Class<?> parentCls, Class<T> annCls) {
        return parentCls.getDeclaredAnnotation(annCls);
    }
}
