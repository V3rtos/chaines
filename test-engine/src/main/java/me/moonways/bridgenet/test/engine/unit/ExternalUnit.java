package me.moonways.bridgenet.test.engine.unit;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.factory.BeanFactoryProviders;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.apache.logging.log4j.core.util.ReflectionUtil;
import org.junit.runner.notification.RunNotifier;

import java.lang.reflect.Field;

@Getter
@Builder
@ToString
public class ExternalUnit {

    private final Object instance;
    private final BeanFactoryProviders factoryProvider;
    private final Class<?> testClass;
    private final Field field;

    public String getName() {
        return testClass.getSimpleName();
    }

    public void inject(RunNotifier notifier, BeansService beansService, BridgenetJUnitTestRunner runner) {
        Object testObject = factoryProvider.getImpl().get().create(testClass);
        beansService.fakeBind(testObject);

        ReflectionUtil.setFieldValue(field, instance, testObject);

        runner.processing(new TestClassUnit(notifier, testObject));
    }
}
