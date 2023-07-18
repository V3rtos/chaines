package me.moonways.bridgenet.injection;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.proxy.ProxiedObject;
import me.moonways.bridgenet.injection.proxy.intercept.ProxiedObjectInterceptor;
import me.moonways.bridgenet.injection.scanner.DependencyScanner;
import me.moonways.bridgenet.injection.scanner.ScannerFilter;
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

    { bindSelf(); }

    private void bindSelf() {
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
        Class<?> cls = instance.getClass();

        injector.injectFields(instance);

        if (!container.hasInjectionMark(cls) && !container.isComponentFound(cls)) {
            container.markInjected(cls);

            log.info("Applied injection for instance of §6{}", instance.getClass().getName());
        }
    }

    public void bind(@NotNull Object object) {
        this.bind(object.getClass(), object);
    }

    public void bind(@NotNull Class<?> cls, @NotNull Object object) {
        if (container.isComponentFound(cls)) {
            return;
        }

        postBind(cls, object);

        if (cls.isAnnotationPresent(Component.class)) {
            log.info("Bind component instance of §6{}", cls.getName());
        }
    }

    private void postBind(Class<?> cls, Object object) {
        if (cls.isAnnotationPresent(ProxiedObject.class)) {
            injectFields(object);

            ProxiedObjectInterceptor interceptor = ProxiedObjectInterceptor.intercept(object);

            container.store(cls, interceptor.createProxy());
            scanner.postProxiedFactoryMethods(cls, object, interceptor);
        }
        else {
            container.store(cls, object);
            injectFields(object);

            scanner.postFactoryMethods(cls, object);
        }
    }
}
