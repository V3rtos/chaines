package me.moonways.bridgenet.api.inject;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObject;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObjectProxy;
import me.moonways.bridgenet.api.inject.scanner.DependencyScanner;
import me.moonways.bridgenet.api.inject.scanner.ScannerFilter;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;

@Log4j2
public class DependencyInjection {

    @Getter
    private final DependencyContainer container = new DependencyContainer();

    @Getter
    private final DependencyScanner scanner = new DependencyScanner();

    @Getter
    private final FieldInjectManager injector = new FieldInjectManager(container);

    @Inject
    private AnnotationInterceptor annotationInterceptor;

    { bindSelf(); }

    private void bindSelf() {
        bind(new AnnotationInterceptor());
        bind(this);

        injectFields(container);
        injectFields(injector);
        injectFields(scanner);

        scanner.initContainer();
    }

    public void findComponents(@NotNull Class<? extends Annotation> cls, @NotNull ScannerFilter filter) {
        scanner.resolve(cls, filter);
    }

    public void findComponents(@NotNull ScannerFilter filter) {
        scanner.resolve(Component.class, filter);
    }

    public void findComponents(@NotNull String packageName) {
        scanner.resolve(packageName, Component.class);
    }

    public void findComponents(@NotNull String packageName, @NotNull Class<? extends Annotation> cls) {
        scanner.resolve(packageName, cls);
    }

    public void findComponentsIntoBasePackage() {
        String searchGeneralPackage = scanner.getSearchGeneralPackage();
        scanner.resolve(searchGeneralPackage, Component.class);
    }

    public void findComponentsIntoBasePackage(@NotNull Class<? extends Annotation> cls) {
        String searchGeneralPackage = scanner.getSearchGeneralPackage();
        scanner.resolve(searchGeneralPackage, cls);
    }

    public void injectFields(@NotNull Object instance) {
        injector.injectFields(instance);
    }

    public void bind(@NotNull Object object) {
        this.bind(object.getClass(), object);
    }

    public void bind(@NotNull Class<?> bindClass, @NotNull Object object) {
        if (container.isComponentFound(bindClass)) {
            return;
        }

        postBind(bindClass, object);

        if (bindClass.isAnnotationPresent(Component.class)) {
            log.info("Bind component instance of §6{}", bindClass.getName());
        }
    }

    private void postBind(Class<?> bindClass, Object object) {
        Class<?> objectClass = object.getClass();

        if (bindClass.isAnnotationPresent(DecoratedObject.class)) {
            injectFields(object);

            DecoratedObjectProxy interceptor = new DecoratedObjectProxy();
            Object proxy = annotationInterceptor.createProxy(object, interceptor);

            container.store(bindClass, proxy);
            scanner.postProxiedFactoryMethods(objectClass, object);
        }
        else {
            container.store(bindClass, object);
            injectFields(object);

            scanner.postFactoryMethods(objectClass, object);
        }
    }
}
