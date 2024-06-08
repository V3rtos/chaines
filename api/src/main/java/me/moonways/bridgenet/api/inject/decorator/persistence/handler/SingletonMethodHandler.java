package me.moonways.bridgenet.api.inject.decorator.persistence.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratedMethodHandler;
import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;

import java.util.Map;
import java.util.WeakHashMap;

@Log4j2
public class SingletonMethodHandler implements DecoratedMethodHandler {

    private static final Map<String, Object> SINGLETONS = new WeakHashMap<>();

    @Override
    public Object handleProxyInvocation(DecoratorInvocation invocation) {
        if (invocation.isVoid()) {
            log.error("§4Cannot invoke @Singleton decorator for {}: §cConflict with VOID return-type", invocation);
            return null;
        }

        Object value = SINGLETONS.computeIfAbsent(invocation.toString(), (k) -> invocation.proceed());
        log.debug("§3Decorated method {} was returned same value", invocation);

        return value;
    }
}
