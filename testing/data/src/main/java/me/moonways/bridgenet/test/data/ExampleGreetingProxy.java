package me.moonways.bridgenet.test.data;

import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@MethodInterceptor
public class ExampleGreetingProxy {

    @MethodHandler(target = ExampleGreetingAnnotation.class)
    public Object handle(ProxiedMethod method, Object[] args) {
        return TestConst.Interceptor.HELLO_MESSAGE;
    }
}
