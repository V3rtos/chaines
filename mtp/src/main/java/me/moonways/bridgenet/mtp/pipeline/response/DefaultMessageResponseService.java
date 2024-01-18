package me.moonways.bridgenet.mtp.pipeline.response;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

@Log4j2
@Autobind
@SuppressWarnings("unchecked")
public final class DefaultMessageResponseService {

    private final Set<ResponseDescriptor> responseDescriptorSet = new CopyOnWriteArraySet<>();

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
            log.info("§4Awaited response message §3{} §4has timed out", descriptor.responseType);
            descriptor.completeExceptionally(
                    new MessageResponseTimeoutException(descriptor.toString()));
        }
    }

    /**
     * Ожидается ли ответ указанного типа сообщения.
     * @param responseType - класс ожидаемого сообщения.
     */
    public boolean isWaiting(@NotNull Class<?> responseType) {
        return responseDescriptorSet.stream()
                .filter(responseDescriptor -> !responseDescriptor.isExpired())
                .anyMatch(responseDescriptor -> responseDescriptor.isSimilar(responseType));
    }

    /**
     * Поставить указанный тип сообщения респонса в ожидание.
     *
     * @param future - исполняемый процесс при получении респонса.
     * @param responseType - ожидаемый тип сообщения.
     */
    public void await(int timeout, @NotNull CompletableFuture<?> future, @NotNull Class<?> responseType) {
        log.info("Responsible message §3{} §rsaved as awaited response by §6{}ms §rtimeout", responseType, timeout);

        if (isWaiting(responseType)) {
            Set<ResponseDescriptor> result = responseDescriptorSet.stream()
                    .filter(responseDescriptor -> responseDescriptor.timeout == timeout)
                    .filter(responseDescriptor -> responseDescriptor.isSimilar(responseType))
                    .collect(Collectors.toSet());

            if (!result.isEmpty()) {
                result.forEach(responseDescriptor -> responseDescriptor.futures.add((CompletableFuture<Object>) future));
                return;
            }
        }

        List<CompletableFuture<Object>> futures = new LinkedList<>();
        futures.add((CompletableFuture<Object>) future);

        responseDescriptorSet.add(
                new ResponseDescriptor(System.currentTimeMillis() + timeout, timeout,
                        futures, responseType));
    }

    /**
     * Выполнить ожидаемые запросы на респонс исходя
     * из входящего сообщения.
     *
     * @param inputMessage - входящее сообщение
     */
    public void complete(@NotNull Object inputMessage) {
        List<ResponseDescriptor> completed = new ArrayList<>();
        Class<?> inputMessageClass = inputMessage.getClass();

        for (ResponseDescriptor descriptor : responseDescriptorSet) {
            if (descriptor.isSimilar(inputMessageClass)) {

                descriptor.complete(inputMessage);
                completed.add(descriptor);
            }
        }

        if (!completed.isEmpty()) {
            responseDescriptorSet.removeIf(completed::contains);
            log.info("§2Awaited response message §3{} §2was completed", inputMessage.getClass());
        }
    }

    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    @RequiredArgsConstructor
    private static class ResponseDescriptor {

        private final long expireIn;

        @EqualsAndHashCode.Include
        private final int timeout;

        @Getter
        private final List<CompletableFuture<Object>> futures;
        @Getter
        @EqualsAndHashCode.Include
        private final Class<?> responseType;

        public boolean isExpired() {
            return expireIn <= System.currentTimeMillis();
        }

        public boolean isSimilar(Class<?> responseType) {
            return this.responseType.isAssignableFrom(responseType);
        }

        public void complete(Object message) {
            futures.forEach(completableFuture -> completableFuture.complete(message));
        }

        public void completeExceptionally(Throwable exception) {
            futures.forEach(completableFuture -> completableFuture.completeExceptionally(exception));
        }
    }
}
