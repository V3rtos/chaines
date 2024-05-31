package me.moonways.bridgenet.test.engine.flow;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.TestEngineException;
import me.moonways.bridgenet.test.engine.TestEngineExceptionFormatter;
import me.moonways.bridgenet.test.engine.TestingObject;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
@Getter
@Builder
@ToString
public class TestFlowContext {

    private static final TestEngineExceptionFormatter EXCEPTION_FORMATTER
            = new TestEngineExceptionFormatter();

    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KeyRegistry<T> {

        public static <T> KeyRegistry<T> typed(String name, Class<T> returnType) {
            return new KeyRegistry<>(name, returnType);
        }

        public static <T> KeyRegistry<T> named(String name) {
            return typed(name, null);
        }

        @EqualsAndHashCode.Include
        private final String name;
        private final Class<T> returnType;
    }

    public static final KeyRegistry<AppBootstrap> BOOTSTRAP = KeyRegistry.named("system_bootstrap");
    public static final KeyRegistry<BeansService> BEANS = KeyRegistry.named("beans_managements");

    private final ModernTestEngineRunner runner;
    private final List<TestFlowNode> flowNodes;
    private final TestFlowProcessor processor;
    private final TestClass testClass;
    private final RunNotifier runNotifier;
    private final TestingObject testingObject;

    private final ExecutorService forkJoinPool
            = Executors.newWorkStealingPool();

    private final Map<KeyRegistry<?>, Object> processingInstancesMap
            = Collections.synchronizedMap(new ConcurrentHashMap<>());

    public <T> void setInstance(KeyRegistry<T> registry, T instance) {
        processingInstancesMap.put(registry, instance);
    }

    public <T> Optional<T> getInstance(KeyRegistry<T> registry) {
        //noinspection unchecked
        return Optional.ofNullable((T) processingInstancesMap.get(registry));
    }

    public void throwException(Throwable exception) {
        log.error(EXCEPTION_FORMATTER.formatToString(new TestEngineException(exception.getMessage(), exception)));
    }

    public static void sendEngineGreetingMessage() {
        System.out.println();
        System.out.println("\\\\==============-------------------------        <  TEST-ENGINE  >        -------------------------==============//");
        System.out.println("\\\\-----------================----------  :: RUNNING BRIDGENET EMULATION ::  --------================-------------//");
        System.out.println();
    }

    public static void sendWarnOfErrorMessage() {
        System.out.println();
        System.out.println("\\\\==============-------------------------        >  TEST-ENGINE  <        -------------------------==============//");
        System.out.println("\\\\-----------================----------  :: UNIT IS DOWN BY FATAL ERROR ::  --------================-------------//");
        System.out.println();
    }

    public static void sendPassedMessage() {
        System.out.println();
        System.out.println("\\\\==============-------------------------        >  TEST-ENGINE  <        -------------------------==============//");
        System.out.println("\\\\-----------================----------  :: UNIT IS PASSED WAS SUCCESS! ::  --------================-------------//");
        System.out.println();
    }
}
