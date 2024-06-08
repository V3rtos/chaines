package me.moonways.bridgenet.api.proxy;

import javassist.util.proxy.ProxyFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.proxy.proxy.InterfaceProxy;
import me.moonways.bridgenet.api.proxy.proxy.ProxyManager;
import me.moonways.bridgenet.api.proxy.proxy.SuperclassProxy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Arrays;

@Log4j2
@RequiredArgsConstructor
public class InterceptController {

    private final ClassLoader classLoader;

    private final Class<?> cls;
    private final Object source;

    private final Object interceptor;

    private void prepareInterceptor() {
        Arrays.stream(interceptor.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(InterceptionFactory.class))
                .forEachOrdered(method -> {

                    ProxiedMethod proxiedMethod = ProxiedMethod.create(interceptor, method);
                    proxiedMethod.callEmpty();
                });
    }

    public synchronized Object createProxy() {
        if (cls.isInterface()) {
            prepareInterceptor();

            return createInterfaceProxy();
        } else {
            return createSuperclassProxy();
        }
    }

    private Object createInterfaceProxy() {
        return Proxy.newProxyInstance(classLoader,
                new Class[]{cls},
                new InterfaceProxy(interceptor, cls));
    }

    private Object createSuperclassProxy() {
        ProxyFactory proxyFactory = new ProxyFactory();

        proxyFactory.setSuperclass(cls);
        proxyFactory.setUseWriteReplace(true);
        proxyFactory.setUseCache(false);

        try {
            Object source = this.source;
            if (source == null) {
                source = cls.getConstructor().newInstance();
            }

            ProxyManager proxyManager = new ProxyManager(source, cls);
            SuperclassProxy superclassProxy = new SuperclassProxy(interceptor, proxyManager);

            prepareInterceptor();
            return proxyFactory.create(new Class[0], new Object[0], superclassProxy);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException |
                 InvocationTargetException exception) {

            log.error("§4Cannot be create component proxy: §c{}", exception.toString());
        }

        return null;
    }
}
