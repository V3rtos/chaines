package me.moonways.api.proxy;

import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@MethodInterceptor
public class GreetingProxy {

    @MethodHandler(target = Greeting.class)
    public Object handle(ProxiedMethod method, Object[] args) {
        System.out.println("Hello world!");

        return method.call(args);
    }
}
