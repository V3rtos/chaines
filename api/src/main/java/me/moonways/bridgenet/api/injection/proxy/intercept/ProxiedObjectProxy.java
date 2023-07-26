package me.moonways.bridgenet.api.injection.proxy.intercept;

import me.moonways.bridgenet.api.intercept.MethodHandler;
import me.moonways.bridgenet.api.intercept.MethodInterceptor;
import me.moonways.bridgenet.api.intercept.InterceptionFactory;
import me.moonways.bridgenet.api.intercept.ProxiedMethod;

import java.lang.annotation.Annotation;

@MethodInterceptor
public class ProxiedObjectProxy {

    private final ProxiedMethodScanner proxiedMethodScanner = new ProxiedMethodScanner();

    @InterceptionFactory
    void onInit() {
        proxiedMethodScanner.injectProxiedHandlers();
    }

    @MethodHandler
    public Object handle(ProxiedMethod proxiedMethod, Object[] args) {
        for (Class<?> annotationType : proxiedMethodScanner.getAnnotationsTypes()) {
            Class<? extends Annotation> annotation = annotationType.asSubclass(Annotation.class);

            if (proxiedMethod.hasAnnotation(annotation)) {

                ProxiedMethodHandler handler = proxiedMethodScanner.findMethodHandler(annotation);
                return handler.handleProxyInvocation(proxiedMethod, args);
            }
        }
        return proxiedMethod.call(args);
    }
}
