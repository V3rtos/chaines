package me.moonways.bridgenet.injection.proxy.intercept;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Log4j2
@Getter
@EqualsAndHashCode
@RequiredArgsConstructor
public class ProxiedMethod {

    private static final String METHOD_NAME_FORMAT = "%s.%s();";


    private final Object source;

    private final String name;
    private final Class<?> returnType;

    private final Method declare;

    public synchronized Object call(@Nullable Object[] args) {
        try {
            declare.setAccessible(true);
            return declare.invoke(source, args);
        }
        catch (IllegalAccessException | InvocationTargetException exception) {
            log.error("§4Cannot be invoke proxied method {}: §c{}", this, exception.toString());
            return null;
        }
    }

    public synchronized boolean isVoid() {
        return returnType.equals(void.class);
    }

    @Override
    public String toString() {
        return String.format(METHOD_NAME_FORMAT, source.getClass().getSimpleName(), name);
    }
}
