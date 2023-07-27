package me.moonways.bridgenet.api.intercept;

import me.moonways.bridgenet.api.intercept.proxy.ProxyManager;

import java.lang.reflect.Method;

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

    public void callProxiedSuperclassMethod(Object source, Object proxy, Method method, Object[] args) {
        ProxyManager proxyManager = new ProxyManager(source, source.getClass());
        proxyManager.fireMethodHandler(proxy,
                ProxiedMethod.create(source, method), args);
    }
}
