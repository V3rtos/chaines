package me.moonways.bridgenet.mtp.message.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Log4j2
@Autobind
@SuppressWarnings("unchecked")
public final class ResponsibleMessageService {

    private final Set<ResponseDescriptor> responseDescriptorSet = new CopyOnWriteArraySet<>();
    private final AtomicLong callbackIdSeed = new AtomicLong(1);

    /**
     * Сгенерировать уникальный идентификатор
     * ответа на ожидаемое сообщение.
     */
    public long generateCallbackID() {
        long next = callbackIdSeed.getAndIncrement();
        if (next == Long.MAX_VALUE) {
            callbackIdSeed.set(1);
        }
        return next;
    }

    /**
     * Очистить истекшие по таймауту
     * ожидаемые респонсы из кеша.
     */
    public void cleanUp() {
        Set<ResponseDescriptor> removed = responseDescriptorSet.stream()
                .filter(ResponseDescriptor::isExpired)
                .collect(Collectors.toSet());

        responseDescriptorSet.removeIf(removed::contains);

        for (ResponseDescriptor descriptor : removed) {
            log.info("§4Awaited response message §3{} §4has timed out", descriptor.responseType.getName());
            descriptor.completeExceptionally(
                    new ResponsibleMessageTimeoutException(descriptor.toString()));
        }
    }

    /**
     * Ожидается ли ответ указанного типа сообщения.
     *
     * @param responseType - класс ожидаемого сообщения.
     */
    public boolean isWaiting(long id, @NotNull Class<?> responseType) {
        return responseDescriptorSet.stream()
                .filter(responseDescriptor -> !responseDescriptor.isExpired())
                .anyMatch(responseDescriptor -> responseDescriptor.isForThis(id, responseType));
    }

    /**
     * Поставить указанный тип сообщения респонса в ожидание.
     *
     * @param future       - исполняемый процесс при получении респонса.
     * @param responseType - ожидаемый тип сообщения.
     */
    public void await(int timeout, long id, @NotNull CompletableFuture<?> future, @NotNull Class<?> responseType) {
        log.info("Responsible message §3{} §rsaved as awaited response by §6{}ms §rtimeout", responseType.getName(), timeout);

        if (isWaiting(id, responseType)) {
            Set<ResponseDescriptor> result = responseDescriptorSet.stream()
                    .filter(responseDescriptor -> responseDescriptor.timeout == timeout)
                    .filter(responseDescriptor -> responseDescriptor.isForThis(id, responseType))
                    .collect(Collectors.toSet());

            if (!result.isEmpty()) {
                result.forEach(responseDescriptor -> responseDescriptor.futures.add((CompletableFuture<Object>) future));
                return;
            }
        }

        List<CompletableFuture<Object>> futures = Collections.synchronizedList(new LinkedList<>());
        futures.add((CompletableFuture<Object>) future);

        responseDescriptorSet.add(
                new ResponseDescriptor(System.currentTimeMillis() + timeout, timeout,
                        id, futures, responseType));
    }

    /**
     * Выполнить ожидаемые запросы на респонс исходя
     * из входящего сообщения.
     *
     * @param callbackID - идентификатор ответа на сообщение
     * @param inputMessage - входящее сообщение
     */
    public void complete(long callbackID, @NotNull Object inputMessage) {
        List<ResponseDescriptor> completed = new ArrayList<>();
        Class<?> inputMessageClass = inputMessage.getClass();

        for (ResponseDescriptor descriptor : responseDescriptorSet) {
            if (descriptor.isForThis(callbackID, inputMessageClass)) {

                descriptor.complete(inputMessage);
                completed.add(descriptor);
            }
        }

        if (!completed.isEmpty()) {
            responseDescriptorSet.removeIf(completed::contains);
            log.info("Awaited response message §2{} §rwas completed", inputMessage.getClass().getName());
        }
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor
    private static class ResponseDescriptor {

        private final long expireIn;
        private final int timeout;

        @EqualsAndHashCode.Include
        private final long callbackID;

        @Getter
        private final List<CompletableFuture<Object>> futures;
        @Getter
        @EqualsAndHashCode.Include
        private final Class<?> responseType;

        public boolean isExpired() {
            return expireIn <= System.currentTimeMillis();
        }

        public boolean isForThis(long callbackID, Class<?> responseType) {
            return Objects.equals(this.callbackID, callbackID) && isTypeSimilar(responseType);
        }

        private boolean isTypeSimilar(Class<?> responseType) {
            return this.responseType.equals(responseType) || this.responseType.isAssignableFrom(responseType);
        }

        public void complete(Object message) {
            futures.forEach(completableFuture -> completableFuture.complete(message));
        }

        public void completeExceptionally(Throwable exception) {
            futures.forEach(completableFuture -> completableFuture.completeExceptionally(exception));
        }
    }
}
