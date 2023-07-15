package me.moonways.bridgenet.injection;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.scanner.DependencyScanner;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@Log4j2
public class DependencyInjection {

    private static final String GENERAL_PACKAGE = "me.moonways";

    @Getter
    private final DependencyContainer container = new DependencyContainer();

    @Getter
    private final DependencyScanner scanner = new DependencyScanner();

    @Getter
    private final FieldInjectManager injector = new FieldInjectManager(container);

    { bindSelf(); }

    private void bindSelf() {
        bind(this);

        injectFields(container);
        injectFields(injector);
        injectFields(scanner);

        scanner.initContainer();
    }

    public void findComponents(@NotNull String packageName) {
        scanner.resolve(packageName, Component.class);
    }

    public void findComponents(@NotNull String packageName, @NotNull Class<? extends Annotation> cls) {
        scanner.resolve(packageName, cls);
    }

    public void findComponentsIntoBasePackage() {
        scanner.resolve(GENERAL_PACKAGE, Component.class);
    }

    public void findComponentsIntoBasePackage(@NotNull Class<? extends Annotation> cls) {
        scanner.resolve(GENERAL_PACKAGE, cls);
    }

    public void injectFields(@NotNull Object instance) {
        Class<?> cls = instance.getClass();

        injector.injectFields(instance);

        if (!container.hasInjectionMark(cls) && !container.isComponentFound(cls)) {

            container.markInjected(cls);
            log.info("Injected fields for class: §6" + instance.getClass().getName());
        }
    }

    public void bind(@NotNull Object object) {
        this.bind(object.getClass(), object);
    }

    public void bind(@NotNull Class<?> cls, @NotNull Object object) {
        if (container.isComponentFound(cls)) {
            return;
        }

        container.store(cls, object);
        injectFields(object);

        scanner.postFactoryMethods(cls, object);

        if (cls.isAnnotationPresent(Component.class)) {
            log.info("Bind found component: §6" + cls.getName());
        }
    }
}
