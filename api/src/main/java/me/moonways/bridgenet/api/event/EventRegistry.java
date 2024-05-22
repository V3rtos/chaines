package me.moonways.bridgenet.api.event;

import org.jetbrains.annotations.NotNull;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class EventRegistry {

    private final MethodHandles.Lookup publicLookup = MethodHandles.publicLookup();
    private final Map<Class<?>, Set<EventInvoker<?>>> eventHandlersSet = Collections.synchronizedMap(new HashMap<>());

    private void validateNull(Object handler) {
        if (handler == null) {
            throw new EventException("registered handler is null");
        }
    }

    private void validateHandlerNull(Class<?> handlerType) {
        if (handlerType == null) {
            throw new EventException("handler type is null");
        }
    }

    private void validateEventNull(Class<? extends Event> eventType) {
        if (eventType == null) {
            throw new EventException("event type is null");
        }
    }

    private void validateEmptyHandlers(Object handler) {
        Arrays.stream(handler.getClass().getDeclaredMethods())
                .filter(method -> method.isAnnotationPresent(EventHandle.class))
                .findFirst()
                .orElseThrow(() -> new EventException("registered handler is not contain event-handlers"));
    }

    public void register(Object handler) {
        validateNull(handler);

        Set<EventInvoker<?>> invokerSet = findHandlers(handler);
        eventHandlersSet.put(handler.getClass(), invokerSet);
    }

    public void unregister(Class<?> handlerType) {
        validateHandlerNull(handlerType);
        eventHandlersSet.remove(handlerType);
    }

    private Set<EventInvoker<?>> findHandlers(Object handler) {
        validateEmptyHandlers(handler);
        return findHandlersReflect(handler);
    }

    private Set<EventInvoker<?>> findHandlersReflect(Object handler) {
        Set<EventInvoker<?>> resultSet = new HashSet<>();

        final Class<?> handlerType = handler.getClass();
        final Method[] methodsArray = handlerType.getDeclaredMethods();

        for (Method method : methodsArray) {
            try {
                EventInvoker<?> invoker = injectReflectedEventHandle(handler, method);
                resultSet.add(invoker);
            } catch (IllegalAccessException exception) {
                throw new EventException(exception, "Internal handler registration error - {0}", handler);
            }
        }

        return resultSet;
    }

    @SuppressWarnings("unchecked")
    private EventInvoker<?> injectReflectedEventHandle(Object handler, Method method) throws IllegalAccessException {
        EventHandle annotation = method.getDeclaredAnnotation(EventHandle.class);
        if (annotation == null) {
            return null;
        }

        Class<?>[] parameterTypes = method.getParameterTypes();

        if (parameterTypes.length != 1 || !Event.class.isAssignableFrom(parameterTypes[0])) {
            return null;
        }

        Class<? extends Event> eventType = (Class<? extends Event>) parameterTypes[0];
        return new EventInvoker<>(handler, annotation.priority(), eventType, publicLookup.unreflect(method));
    }

    @NotNull
    public Set<EventInvoker<?>> findInvokers(@NotNull Class<? extends Event> eventType) {
        validateEventNull(eventType);
        return eventHandlersSet
                .values()
                .parallelStream()
                .flatMap(Collection::stream)
                .filter(Objects::nonNull)
                .filter(invoker -> invoker.isTyped(eventType))
                .collect(Collectors.toSet());
    }

    @NotNull
    public Set<EventInvoker<?>> findInvokersByPriority(@NotNull Class<? extends Event> eventType) {
        validateNull(eventType);
        return findInvokers(eventType)
                .parallelStream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(event -> event.getPriority().ordinal())))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
