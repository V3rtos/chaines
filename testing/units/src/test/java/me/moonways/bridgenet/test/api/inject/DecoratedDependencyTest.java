package me.moonways.bridgenet.test.api.inject;

import lombok.SneakyThrows;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.bean.service.BeansStore;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.*;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(ModernTestEngineRunner.class)
public class DecoratedDependencyTest {

    @Inject
    private AnyObjectWithDecorators subj;

    @Inject
    private BeansService beansService;
    @Inject
    private BeansStore beansStore;

    @Before
    public void setUp() {
        beansService.bind(new AnyObjectWithDecorators());
        subj = beansStore.find(AnyObjectWithDecorators.class)
                .map(bean -> ((AnyObjectWithDecorators) bean.getRoot())).orElse(null);
    }

    @Test
    public void test_async() {
        String threadName = subj.testAsync();
        assertNotEquals(Thread.currentThread().getName(), threadName);
    }

    @Test
    public void test_singleton() {
        assertEquals(subj.testSingleton(), subj.testSingleton(), 0);
    }

    @EnableDecorators
    public static class AnyObjectWithDecorators {

        @LateExecution(delay = 4, unit = TimeUnit.SECONDS)
        public String testStringLateExecution() {
            return "testStringLateExecution";
        }

        @KeepTime
        @SneakyThrows
        public void testKeepTime() {
            Thread.sleep(3000);
        }

        @Async
        public String testAsync() {
            return Thread.currentThread().getName();
        }

        @Singleton
        public double testSingleton() {
            return Math.PI * Math.random() + Math.sin(90);
        }

        @RequiredNotNull(message = "testRequiredNotNullWithCustomMessage() has received null")
        public Object testRequiredNotNullWithCustomMessage() {
            return null;
        }
    }
}
