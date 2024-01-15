package me.moonways.bridgenet.test.api.injection;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.api.proxy.MethodHandler;
import me.moonways.bridgenet.api.proxy.MethodInterceptor;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.junit.Assert.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class ProxyClassInterceptionTest {

    @Inject
    private AnnotationInterceptor interceptor;

    @Test
    public void test_sayHello() {
        GreetingService greetingService = interceptor.createProxyChecked(GreetingService.class, new GreetingProxy());

        assertEquals(greetingService.sayHello(), "Hello world!");
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.METHOD)
    public @interface Greeting {
    }

    public interface GreetingService {

        @Greeting
        String sayHello();
    }

    @MethodInterceptor
    public class GreetingProxy {

        @MethodHandler(target = Greeting.class)
        public Object handle(ProxiedMethod method, Object[] args) {
            return "Hello world!";
        }
    }
}
