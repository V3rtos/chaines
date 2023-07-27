package me.moonways.bridgenet.api.proxy.proxy;

import javassist.util.proxy.MethodHandler;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;

@RequiredArgsConstructor
public class SuperclassProxy implements MethodHandler {

    private final Object interceptor;
    private final ProxyManager proxyManager;

    @Override
    public Object invoke(Object self, Method thisMethod, Method proceed, Object[] args) throws Throwable {
        return proxyManager.invoke(thisMethod, interceptor, args);
    }
}
