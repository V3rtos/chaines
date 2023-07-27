package me.moonways.bridgenet.api.inject.decorator.handler;

import java.util.Map;
import java.util.WeakHashMap;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.proxy.DecoratedMethodHandler;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@Log4j2
public class SingletonMethodHandler implements DecoratedMethodHandler {

    private static final Map<String, Object> SINGLETONS = new WeakHashMap<>();

    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        if (proxiedMethod.isVoid()) {
            log.info("§4Cannot invoke @Singleton decorator for {}: §cConflict with VOID return-type", proxiedMethod);

            return proxiedMethod.call(args);
        }

        Object value = SINGLETONS.computeIfAbsent(proxiedMethod.toString(), (k) -> proxiedMethod.call(args));
        log.info("§3Decorated method {} returned Singleton object value", proxiedMethod);

        return value;
    }
}
