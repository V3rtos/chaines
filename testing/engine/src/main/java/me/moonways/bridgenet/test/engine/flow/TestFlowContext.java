package me.moonways.bridgenet.test.engine.flow;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.TestEngineException;
import me.moonways.bridgenet.test.engine.TestEngineExceptionFormatter;
import me.moonways.bridgenet.test.engine.TestingObject;
import me.moonways.bridgenet.test.engine.component.module.Module;
import me.moonways.bridgenet.test.engine.component.step.Step;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.TestClass;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Класс TestFlowContext представляет контекст для выполнения тестов, обеспечивая хранение и управление состоянием
 * и данными, связанными с тестовыми потоками.
 */
@Log4j2
@Getter
@Builder
@ToString
public class TestFlowContext {

    private static final TestEngineExceptionFormatter EXCEPTION_FORMATTER = new TestEngineExceptionFormatter();

    /**
     * Класс KeyRegistry представляет реестр ключей для хранения и доступа к экземплярам различных типов в контексте.
     *
     * @param <T> тип значений, ассоциированных с ключом.
     */
    @ToString
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class KeyRegistry<T> {

        /**
         * Создает KeyRegistry с указанным именем и типом возвращаемого значения.
         *
         * @param name имя реестра.
         * @param returnType тип возвращаемого значения.
         * @param <T> тип значений, ассоциированных с ключом.
         * @return новый экземпляр KeyRegistry.
         */
        public static <T> KeyRegistry<T> typed(String name, Class<T> returnType) {
            return new KeyRegistry<>(name, returnType);
        }

        /**
         * Создает KeyRegistry с указанным именем без указания типа возвращаемого значения.
         *
         * @param name имя реестра.
         * @param <T> тип значений, ассоциированных с ключом.
         * @return новый экземпляр KeyRegistry.
         */
        public static <T> KeyRegistry<T> named(String name) {
            return typed(name, null);
        }

        @EqualsAndHashCode.Include
        private final String name;
        private final Class<T> returnType;
    }

    public static final KeyRegistry<AppBootstrap> BOOTSTRAP = KeyRegistry.named("system_bootstrap");
    public static final KeyRegistry<BeansService> BEANS = KeyRegistry.named("beans_managements");
    public static final KeyRegistry<List<Module>> LOADED_MODULES = KeyRegistry.named("loaded_modules");
    public static final KeyRegistry<List<Step>> BEFORE_STEPS = KeyRegistry.named("before_steps");

    private final ModernTestEngineRunner runner;
    private final List<TestFlowNode> flowNodes;
    private final TestFlowProcessor processor;
    private final TestClass testClass;
    private final RunNotifier runNotifier;
    private final TestingObject testingObject;

    private final Map<KeyRegistry<?>, Object> processingInstancesMap
            = Collections.synchronizedMap(new ConcurrentHashMap<>());

    /**
     * Устанавливает экземпляр для указанного реестра ключей.
     *
     * @param registry реестр ключей.
     * @param instance экземпляр, который необходимо сохранить.
     * @param <T> тип значения.
     */
    public <T> void setInstance(KeyRegistry<T> registry, T instance) {
        processingInstancesMap.put(registry, instance);
    }

    /**
     * Возвращает экземпляр для указанного реестра ключей.
     *
     * @param registry реестр ключей.
     * @param <T> тип значения.
     * @return Optional с экземпляром, если он существует, или пустой Optional.
     */
    public <T> Optional<T> getInstance(KeyRegistry<T> registry) {
        //noinspection unchecked
        return Optional.ofNullable((T) processingInstancesMap.get(registry));
    }

    /**
     * Возвращает узел потока тестирования указанного класса.
     *
     * @param nodeClass класс узла потока.
     * @param <N> тип узла потока.
     * @return Optional с узлом потока, если он существует, или пустой Optional.
     */
    public <N extends TestFlowNode> Optional<N> getFlowNode(Class<N> nodeClass) {
        return flowNodes.stream()
                .filter(testFlowNode -> testFlowNode.getClass().equals(nodeClass))
                .findFirst()
                .map(testFlowNode -> (N) testFlowNode);
    }

    /**
     * Выбрасывает исключение с указанной ошибкой.
     *
     * @param exception исключение, которое необходимо выбросить.
     */
    public void throwException(Throwable exception) {
        log.error(EXCEPTION_FORMATTER.formatToString(new TestEngineException(exception.getMessage(), exception)));
    }

    /**
     * Отправляет приветственное сообщение от тестового движка.
     */
    public static void sendEngineGreetingMessage() {
        System.out.println(
                "\n" +
                        "▄▄▄█████▓▓█████   ██████ ▄▄▄█████▓       ▓█████ ███▄    █   ▄████  ██▓ ███▄    █▓█████ \n" +
                        "▓  ██▒ ▓▒▓█   ▀ ▒██    ▒ ▓  ██▒ ▓▒       ▓█   ▀ ██ ▀█   █  ██▒ ▀█▒▓██▒ ██ ▀█   █▓█   ▀ \n" +
                        "▒ ▓██░ ▒░▒███   ░ ▓██▄   ▒ ▓██░ ▒░       ▒███  ▓██  ▀█ ██▒▒██░▄▄▄░▒██▒▓██  ▀█ ██▒███   \n" +
                        "░ ▓██▓ ░ ▒▓█  ▄   ▒   ██▒░ ▓██▓ ░        ▒▓█  ▄▓██▒  ▐▌██▒░▓█  ██▓░██░▓██▒  ▐▌██▒▓█  ▄ \n" +
                        "  ▒██▒ ░ ░▒████▒▒██████▒▒  ▒██▒ ░        ░▒████▒██░   ▓██░░▒▓███▀▒░██░▒██░   ▓██░▒████▒\n" +
                        "  ▒ ░░   ░░ ▒░ ░▒ ▒▓▒ ▒ ░  ▒ ░░          ░░ ▒░ ░ ▒░   ▒ ▒  ░▒   ▒ ░▓  ░ ▒░   ▒ ▒░░ ▒░ ░\n" +
                        "    ░     ░ ░  ░░ ░▒  ░ ░    ░            ░ ░  ░ ░░   ░ ▒░  ░   ░  ▒ ░░ ░░   ░ ▒░░ ░  ░\n" +
                        "  ░         ░   ░  ░  ░    ░                ░     ░   ░ ░ ░ ░   ░  ▒ ░   ░   ░ ░   ░   \n" +
                        "            ░  ░      ░                     ░  ░        ░       ░  ░           ░   ░  ░\n" +
                        "                                                                                       \n");
    }

    /**
     * Отправляет сообщение об ошибке тестового движка.
     */
    public static void sendWarnOfErrorMessage() {
        System.out.println();
        System.out.println("\\\\==============-------------------------        >  TEST-ENGINE  <        -------------------------==============//");
        System.out.println("\\\\-----------================----------  :: UNIT IS DOWN BY FATAL ERROR ::  --------================-------------//");
        System.out.println();
    }

    /**
     * Отправляет сообщение об успешном прохождении теста.
     */
    public static void sendPassedMessage() {
        System.out.println();
        System.out.println("\\\\==============-------------------------        >  TEST-ENGINE  <        -------------------------==============//");
        System.out.println("\\\\-----------================----------  :: UNIT IS PASSED WAS SUCCESS! ::  --------================-------------//");
        System.out.println();
    }
}
