package me.moonways.bridgenet.injection.proxy.intercept;

import javassist.util.proxy.MethodHandler;
import javassist.util.proxy.ProxyFactory;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.DependencyInjection;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class ProxiedObjectInterceptor implements MethodHandler {

    public static ProxiedObjectInterceptor intercept(Object source) {
        return new ProxiedObjectInterceptor(source);
    }

    public static <T> T interceptProxy(T source) {
        //noinspection unchecked
        return (T) new ProxiedObjectInterceptor(source).createProxy();
    }

    private final Object source;

    private final ProxiedMethodScanner proxiedMethodScanner = new ProxiedMethodScanner();
    private final Map<String, Method> declaredMethodsMap = new HashMap<>();

    public ProxiedObjectInterceptor(Object source) {
        this.source = source;

        for (Method method : source.getClass().getDeclaredMethods()) {
            declaredMethodsMap.put(method.toString(), method);
        }
    }

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) {
        String methodName = thisMethod.getName();

        Method sourceMethod = declaredMethodsMap.get(thisMethod.toString());
        ProxiedMethod proxiedMethod = new ProxiedMethod(source, methodName, sourceMethod.getReturnType(), sourceMethod);

        for (Class<?> annotationType : proxiedMethodScanner.getAnnotationsTypes()) {
            Class<? extends Annotation> annotation = annotationType.asSubclass(Annotation.class);

            if (sourceMethod.isAnnotationPresent(annotation)) {

                ProxiedMethodHandler handler = proxiedMethodScanner.findMethodHandler(annotation);
                return handler.handleProxyInvocation(proxiedMethod, args);
            }
        }
        return proxiedMethod.call(args);
    }

    public synchronized Object createProxy() {
        ProxyFactory proxyFactory = new ProxyFactory();
        proxyFactory.setSuperclass(source.getClass());

        try {
            proxiedMethodScanner.injectProxiedHandlers();
            return proxyFactory.create(new Class[0], new Object[0], this);
        }
        catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            log.error("§4Cannot be create component proxy: §c{}", exception.toString());
        }

        return null;
    }
}
