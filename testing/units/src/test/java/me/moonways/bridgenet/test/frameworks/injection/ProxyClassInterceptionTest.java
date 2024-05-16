package me.moonways.bridgenet.test.frameworks.injection;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;
import me.moonways.bridgenet.test.data.ExampleGreeting;
import me.moonways.bridgenet.test.data.ExampleGreetingProxy;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.assertEquals;

@RunWith(ModernTestEngineRunner.class)
public class ProxyClassInterceptionTest {

    @Inject
    private AnnotationInterceptor interceptor;

    @Test
    public void test_sayHello() {
        ExampleGreeting greeting = interceptor.createProxyChecked(ExampleGreeting.class, new ExampleGreetingProxy());
        assertEquals(greeting.sayHello(), TestConst.Interceptor.HELLO_MESSAGE);
    }
}
