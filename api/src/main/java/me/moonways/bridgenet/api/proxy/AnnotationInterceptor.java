package me.moonways.bridgenet.api.proxy;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.proxy.proxy.ProxyManager;

import java.lang.reflect.Method;
import java.util.Arrays;

@Autobind
public final class AnnotationInterceptor {

    public InterceptController createController(ClassLoader classLoader, Class<?> cls, Object interceptor) {
        return new InterceptController(classLoader, cls, null, interceptor);
    }

    public InterceptController createController(ClassLoader classLoader, Object source, Object interceptor) {
        return new InterceptController(classLoader, source.getClass(), source, interceptor);
    }

    public Object createProxy(Class<?> cls, Object interceptor) {
        InterceptController controller = createController(cls.getClassLoader(), cls, interceptor);
        return controller.createProxy();
    }

    public Object createProxy(Object object, Object interceptor) {
        InterceptController controller = createController(object.getClass().getClassLoader(), object, interceptor);
        return controller.createProxy();
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxyChecked(Class<T> cls, Object interceptor) {
        return (T) createProxy(cls, interceptor);
    }

    @SuppressWarnings("unchecked")
    public <T> T createProxyChecked(Object object, Object interceptor) {
        return (T) createProxy(object, interceptor);
    }

    public Object callMethodFromProxy(Object source, Object proxy, Method method, Object[] args) {
        ProxyManager proxyManager = new ProxyManager(source, source.getClass());
        return proxyManager.fireMethodHandler(proxy,
                ProxiedMethod.create(source, method), args);
    }

    public Object callProxiedMethod(Object proxy, Method method, Object[] args) {
        Method nativeProxyMethod = Arrays.stream(proxy.getClass().getDeclaredMethods())
                .filter(m -> m.getName().equals(method.getName()))
                .filter(m -> Arrays.equals(m.getParameterTypes(), method.getParameterTypes()))
                .findFirst()
                .orElse(null);

        ProxiedMethod proxiedMethod = ProxiedMethod.create(proxy, nativeProxyMethod);
        return proxiedMethod.call(args);
    }
}
