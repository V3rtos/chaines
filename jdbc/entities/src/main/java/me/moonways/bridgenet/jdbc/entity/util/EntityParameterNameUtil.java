package me.moonways.bridgenet.jdbc.entity.util;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@UtilityClass
public class EntityParameterNameUtil {

    private final Map<Class<?>, String[]> variantsMap = new HashMap<>();

    public Optional<String> fromGetter(Method method) {
        Class<?> declaringClass = method.getDeclaringClass();

        String[] variants = getVariants(declaringClass);

        for (String variant : variants) {
            if (isSimilar(method.getName(), variant)) {
                return Optional.of(variant);
            }
        }

        return Optional.empty();
    }

    private boolean isSimilar(String methodName, String expected) {
        if (methodName.equals(expected)) {
            return true;
        }
        if ((methodName.startsWith("get") || methodName.startsWith("has")) && methodName.charAt(3) == Character.toUpperCase(methodName.charAt(3))) {
            methodName = methodName.substring(3);
        } else if (methodName.startsWith("is") && methodName.charAt(2) == Character.toUpperCase(methodName.charAt(2))) {
            methodName = methodName.substring(2);
        }
        return methodName.equalsIgnoreCase(expected);
    }

    private String[] getVariants(Class<?> declaringClass) {
        String[] variants = variantsMap.get(declaringClass);

        if (variants == null) {
            variantsMap.put(declaringClass, variants =
                    Stream.of(declaringClass.getDeclaredFields())
                            .map(Field::getName)
                            .toArray(String[]::new));
        }

        return variants;
    }
}
