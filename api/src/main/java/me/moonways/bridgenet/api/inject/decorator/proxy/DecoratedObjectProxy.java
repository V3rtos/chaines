package me.moonways.bridgenet.api.inject.decorator.proxy;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.proxy.InterceptionFactory;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@Log4j2
@MethodInterceptor
public class DecoratedObjectProxy {

    private final DecoratedMethodScanner proxiedMethodScanner = new DecoratedMethodScanner();

    @InterceptionFactory
    void onInit() {
        proxiedMethodScanner.injectProxiedHandlers();
    }

    private Set<Object> collectCallingResults(ProxiedMethod proxiedMethod, Object[] args) {
        Set<Object> results = new HashSet<>();

        for (Class<?> annotationType : proxiedMethodScanner.getAnnotationsTypes()) {
            Class<? extends Annotation> annotation = annotationType.asSubclass(Annotation.class);

            if (proxiedMethod.hasAnnotation(annotation)) {

                DecoratedMethodHandler handler = proxiedMethodScanner.findMethodHandler(annotation);
                results.add(handler.handleProxyInvocation(proxiedMethod, args));
            }
        }

        return results;
    }

    @MethodHandler
    public Object handle(ProxiedMethod proxiedMethod, Object[] args) {
        Set<Object> results = collectCallingResults(proxiedMethod, args);
        if (results.isEmpty()) {
            return proxiedMethod.call(args);
        }

        Optional<Object> firstOptional = results.stream()
            .filter(res -> res != null || !proxiedMethod.isVoid())
            .filter(Objects::nonNull)
            .findFirst();

        Object returnObj = firstOptional.orElse(null);
        if (proxiedMethod.isVoid() && returnObj == null) {
            return null;
        }
        else if (returnObj != null) {
            return returnObj;
        }

        log.warn("§4Nothing decorator was returned an anyone value for {}", proxiedMethod);
        return null;
    }
}
