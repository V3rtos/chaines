package me.moonways.bridgenet.api.intercept.proxy;

import lombok.ToString;
import me.moonways.bridgenet.api.intercept.InterceptionException;
import me.moonways.bridgenet.api.intercept.MethodHandler;
import me.moonways.bridgenet.api.intercept.MethodInterceptor;
import me.moonways.bridgenet.api.intercept.ProxiedMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@ToString
public class ProxyManager {

    private final Object source;
    private final Set<ProxiedMethod> proxiedMethodSet = new HashSet<>();

    public ProxyManager(Object source, Class<?> type) {
        this.source = source;
        proxyDeclaredMethods(type);
    }

    private void proxyDeclaredMethods(Class<?> type) {
        Method[] methods = type.getDeclaredMethods();

        for (Method method : methods) {
            if (method.getDeclaredAnnotations().length == 0) {
                continue;
            }

            ProxiedMethod proxiedMethod = ProxiedMethod.create(type.isInterface() ? null : source, method);
            proxiedMethodSet.add(proxiedMethod);
        }
    }

    public Optional<ProxiedMethod> findMethodByName(String name) {
        return proxiedMethodSet.stream()
                .filter(proxiedMethod -> proxiedMethod.getName().equals(name))
                .findFirst();
    }

    public Collection<ProxiedMethod> findMethodsByAnnotation(Class<? extends Annotation> cls) {
        return proxiedMethodSet.stream()
                .filter(proxiedMethod -> proxiedMethod.hasAnnotation(cls))
                .collect(Collectors.toSet());
    }

    public Object fireMethodHandler(Object interceptor, ProxiedMethod method, Object[] args) {

        if (!interceptor.getClass().isAnnotationPresent(MethodInterceptor.class)) {
            throw new InterceptionException("Proxy object " + interceptor.getClass() + " is not marked of @MethodInterceptor annotation");
        }

        Set<ProxiedMethod> methodHandlers = findMethodHandlers(interceptor, method);
        if (methodHandlers.stream().filter(m -> !m.isVoid()).count() > 1) {
            throw new InterceptionException("Count of method-handlers with return type must be 0 or 1 only");
        }

        for (ProxiedMethod proxiedMethodHandler : methodHandlers) {
            Object returnValue = proxiedMethodHandler.call(new Object[]{method, args});

            if (!proxiedMethodHandler.isVoid()) {
                return returnValue;
            }
        }

        return null;
    }

    private Set<ProxiedMethod> findMethodHandlers(Object interceptor, ProxiedMethod method) {
        Set<ProxiedMethod> methodHandlers = new HashSet<>();

        Annotation[] annotations = method.getDeclare().getDeclaredAnnotations();
        Method[] interceptorMethods = interceptor.getClass().getDeclaredMethods();

        for (Method interceptorMethod : interceptorMethods) {
            ProxiedMethod proxiedMethod = ProxiedMethod.create(interceptor, interceptorMethod);

            if (interceptorMethod.isAnnotationPresent(MethodHandler.class)) {

                MethodHandler methodHandler = interceptorMethod.getDeclaredAnnotation(MethodHandler.class);
                Class<? extends Annotation>[] target = methodHandler.target();

                if (target.length == 0 || (target.length == 1 && target[0].equals(Annotation.class))) {
                    methodHandlers.add(proxiedMethod);
                }
                else for (Annotation annotation : annotations) {
                    for (Class<? extends Annotation> interceptorAnnotationType : target) {

                        if (annotation.annotationType().equals(interceptorAnnotationType)) {
                            methodHandlers.add(proxiedMethod);
                        }
                    }
                }
            }
        }

        return methodHandlers;
    }

    public Object invoke(Method method, Object interceptor, Object[] args) {
        AtomicReference<Object> returnValue = new AtomicReference<>();
        findMethodByName(method.getName())
                .ifPresent(proxiedMethod ->
                        returnValue.set(fireMethodHandler(interceptor, proxiedMethod, args)));

        return returnValue.get();
    }
}
