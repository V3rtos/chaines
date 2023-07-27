package me.moonways.api.proxy;

import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;

public class TestStarter {

    public static void main(String[] args) {
        AnnotationInterceptor interceptor = new AnnotationInterceptor();

        TestInterface testInterface = interceptor.createProxyChecked(TestInterface.class, new GreetingProxy());
        TestSuperclass testSuperclass = interceptor.createProxyChecked(TestSuperclass.class, new GreetingProxy());

        testInterface.sayHello();
        testSuperclass.sayHello();
    }
}
