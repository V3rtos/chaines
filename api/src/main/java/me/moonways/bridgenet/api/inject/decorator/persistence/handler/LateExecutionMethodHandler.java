package me.moonways.bridgenet.api.inject.decorator.persistence.handler;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratedMethodHandler;
import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;
import me.moonways.bridgenet.api.inject.decorator.persistence.LateExecution;
import me.moonways.bridgenet.api.util.thread.Threads;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
public class LateExecutionMethodHandler implements DecoratedMethodHandler {
    private final ScheduledExecutorService SCHEDULER
            = Threads.newScheduledThreadPool(2);

    @SneakyThrows
    @Override
    public Object handleProxyInvocation(DecoratorInvocation invocation) {
        LateExecution annotation = invocation.findAnnotation(LateExecution.class);
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();

        SCHEDULER.schedule((Runnable) () -> completableFuture.complete(invocation.proceed()), annotation.delay(), annotation.unit());
        log.debug("§3Running decorated {} scheduled execution: [info={}]", invocation,
                annotation.toString().substring(annotation.annotationType().getName().length() + 1));

        if (invocation.isVoid())
            return null;

        long beginTime = System.currentTimeMillis();
        long terminationTimeout = TimeUnit.MILLISECONDS.convert(annotation.delay(), annotation.unit());

        while (true) {
            if (System.currentTimeMillis() - beginTime >= (terminationTimeout + 100)) {
                return completableFuture.join();
            }
        }
    }
}
