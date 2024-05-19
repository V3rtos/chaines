package me.moonways.bridgenet.mtp.message;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorResult;
import me.moonways.bridgenet.api.inject.processor.persistence.GetTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.persistence.WaitTypeAnnotationProcessor;
import me.moonways.bridgenet.mtp.message.exception.MessageHandleException;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.Priority;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Autobind
@WaitTypeAnnotationProcessor(InboundMessageListener.class)
public class NetworkMessageHandlerList {

    private static final Comparator<MessageSubscriberState> SORTING = Comparator.comparingInt(MessageSubscriberState::getPriority);

    @Getter
    private final List<MessageSubscriberState> subscribers
            = Collections.synchronizedList(new ArrayList<>());

    @Inject
    private BeansService beansService;

    @GetTypeAnnotationProcessor
    private TypeAnnotationProcessorResult<Object> handlersResult;

    public void bindHandlers() {
        handlersResult.toList().forEach(this::bind);
    }

    private List<MessageSubscriberState> toStates(Object handler) {
        int priority = Optional.ofNullable(handler.getClass().getDeclaredAnnotation(InboundMessageListener.class))
                .map(InboundMessageListener::priority)
                .orElse(Priority.MONITOR)
                .ordinal();

        return Arrays.stream(handler.getClass().getDeclaredMethods())
                .filter(method -> method.getDeclaredAnnotation(SubscribeMessage.class) != null)
                .map(method -> new MessageSubscriberState(handler, handler.getClass(), method, priority))
                .collect(Collectors.toList());
    }

    public void bind(Object handler) {
        if (subscribers.addAll(toStates(handler))) {

            log.info("Registered incoming messages listener: §6{}", handler.getClass().getSimpleName());
            beansService.inject(handler);
        }
    }

    public void handle(@NotNull InboundMessageContext<?> context) {
        Class<?> messageClass = context.getMessage().getClass();
        subscribers.sort(SORTING);

        int handlingCount = 0;
        for (MessageSubscriberState subscriber : subscribers) {
            Method method = subscriber.getMethod();

            if (method.getParameterCount() != 1) {
                throw new MessageHandleException(
                        String.format("Can't handle message %s in handler %s", messageClass.getName(),
                                subscriber.getSourceClass().getName()));
            }

            Parameter parameter = method.getParameters()[0];
            try {
                Object value;
                if (parameter.getType().isAssignableFrom(messageClass)) {
                    value = context.getMessage();
                } else if (parameter.getType().equals(InboundMessageContext.class)) {
                    value = context;
                    if (!parameter.getParameterizedType().getTypeName().contains(messageClass.getTypeName())) {
                        continue;
                    }
                } else {
                    continue;
                }

                String handlerClassName = subscriber.getSource().getClass().getSimpleName();

                log.info("Received message §3{} §rredirected to §2{}", messageClass, handlerClassName);

                method.setAccessible(true);
                method.invoke(subscriber.getSource(), value);

                handlingCount++;
            }
            catch (Throwable exception) {
                if (isNotClassCastException(exception)) {
                    throw new MessageHandleException(exception);
                }
            }
        }

        if (handlingCount == 0) {
            log.info("§4No one founded message subscriber for '{}'", messageClass.getName());
        }
    }

    private boolean isNotClassCastException(Throwable exception) {
        return !(exception instanceof ClassCastException) && !(exception.getCause() instanceof ClassCastException);
    }

    @Getter
    @ToString
    @EqualsAndHashCode
    @RequiredArgsConstructor
    public static class MessageSubscriberState {

        private final Object source;
        private final Class<?> sourceClass;

        private final Method method;

        private final int priority;
    }
}
