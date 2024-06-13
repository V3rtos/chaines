package me.moonways.bridgenet.api.proxy.proxy;

import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

@RequiredArgsConstructor
public class InterfaceProxy implements InvocationHandler {

    private final Object interceptor;
    private final Class<?> interfaceClass;

    private ProxyManager proxyManager;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (proxyManager == null) {
            proxyManager = new ProxyManager(null, interfaceClass);
        }

        return proxyManager.invoke(method, interceptor, args);
    }
}
