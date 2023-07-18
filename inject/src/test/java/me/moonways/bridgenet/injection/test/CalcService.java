package me.moonways.bridgenet.injection.test;

import me.moonways.bridgenet.injection.Component;
import me.moonways.bridgenet.injection.proxy.ProxiedAsyncMethod;
import me.moonways.bridgenet.injection.proxy.ProxiedObject;

@Component
@ProxiedObject
public class CalcService {

    @ProxiedAsyncMethod
    public int sum(int a, int b) {
        System.out.println(Thread.currentThread().getName());

        return a + b;
    }
}
