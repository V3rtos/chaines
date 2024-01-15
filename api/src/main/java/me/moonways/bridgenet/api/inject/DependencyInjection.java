package me.moonways.bridgenet.api.inject;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.Decorated;
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

    private final DecoratedObjectProxy decorateProxy = new DecoratedObjectProxy();
    private final AnnotationInterceptor annotationInterceptor = new AnnotationInterceptor();

    { bindSelf(); }

    private void bindSelf() {
        bind(this);
        bind(annotationInterceptor);

        injectFields(container);
        injectFields(injector);
        injectFields(scanner);

        scanner.initContainer();
    }

    public void search(@NotNull Class<? extends Annotation> cls, @NotNull ScannerFilter filter) {
        scanner.resolve(cls, filter);
    }

    public void search(@NotNull ScannerFilter filter) {
        scanner.resolve(Autobind.class, filter);
    }

    public void search(@NotNull String packageName) {
        scanner.resolve(packageName, Autobind.class);
    }

    public void search(@NotNull String packageName, @NotNull Class<? extends Annotation> cls) {
        scanner.resolve(packageName, cls);
    }

    public void searchByProject() {
        String searchGeneralPackage = scanner.getSearchGeneralPackage();
        scanner.resolve(searchGeneralPackage, Autobind.class);
    }

    public void searchByProject(@NotNull Class<? extends Annotation> cls) {
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
        if (container.isStored(bindClass)) {
            return;
        }

        postBind(bindClass, object);

        if (bindClass.isAnnotationPresent(Autobind.class)) {
            log.info("Bind component instance of §6{}", bindClass.getName());
        }
    }

    private void postBind(Class<?> bindClass, Object object) {
        Class<?> objectClass = object.getClass();

        if (bindClass.isAnnotationPresent(Decorated.class)) {
            injectFields(object);

            Object proxy = annotationInterceptor.createProxy(object,
                    decorateProxy);

            container.store(bindClass, proxy);
            scanner.processPostConstruct(objectClass, object);
        }
        else {
            container.store(bindClass, object);
            injectFields(object);

            scanner.processPostConstruct(objectClass, object);
        }
    }

    public void imitateFakeBind(Class<?> bindClass, Object object) {
        scanner.processPreConstructs(bindClass);
        injectFields(object);
        scanner.processPostConstruct(bindClass, object);
    }

    public void imitateFakeBind(Object object) {
        this.imitateFakeBind(object.getClass(), object);
    }
}
