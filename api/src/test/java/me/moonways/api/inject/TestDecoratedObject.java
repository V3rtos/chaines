package me.moonways.api.inject;

import java.util.concurrent.TimeUnit;

import lombok.SneakyThrows;
import me.moonways.bridgenet.api.inject.Depend;
import me.moonways.bridgenet.api.inject.decorator.definition.Async;
import me.moonways.bridgenet.api.inject.decorator.Decorated;
import me.moonways.bridgenet.api.inject.decorator.definition.KeepTime;
import me.moonways.bridgenet.api.inject.decorator.definition.LateExecution;
import me.moonways.bridgenet.api.inject.decorator.definition.RequiredNotNull;
import me.moonways.bridgenet.api.inject.decorator.definition.Singleton;

@Depend
@Decorated
public class TestDecoratedObject {

    @LateExecution(delay = 3000)
    public void testVoidLateExecution() {
        System.out.println("testVoidLateExecution");
    }

    @LateExecution(delay = 4, unit = TimeUnit.SECONDS)
    public String testStringLateExecution() {
        return "testStringLateExecution";
    }

    @KeepTime
    @SneakyThrows
    public void testKeepTime() {
        System.out.println("testKeepTime()");
        Thread.sleep(3000);
    }

    @Async
    public void testAsync() {
        System.out.println("testAsync() -> " + Thread.currentThread().getName());
    }

    @Singleton
    public double testSingleton() {
        System.out.println("testSingleton()");
        return Math.PI * Math.random() + Math.sin(90);
    }

    @RequiredNotNull(printStackTrace = false)
    public Object testRequiredNotNull() {
        return null;
    }

    @RequiredNotNull(message = "testRequiredNotNull() has returned null")
    public Object testRequiredNotNullWithCustomMessage() {
        return null;
    }
}
