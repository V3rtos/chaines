package me.moonways.bridgenet.api.inject.decorator.persistence.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratedMethodHandler;
import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;
import me.moonways.bridgenet.api.util.thread.Threads;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@Log4j2
public class AsyncMethodHandler implements DecoratedMethodHandler {
    private final ExecutorService threadExecutor
            = Threads.newCachedThreadPool();

    @Override
    public Object handleProxyInvocation(DecoratorInvocation invocation) {
        Supplier<Object> asyncExecutorCommand = () -> {

            log.debug("§3Running decorated {} asynchronous execution: [thread={}]", invocation, Thread.currentThread().getName());
            return invocation.proceed();
        };

        if (!invocation.isVoid()) {
            CompletableFuture<Object> objectCompletableFuture
                    = CompletableFuture.supplyAsync(asyncExecutorCommand, threadExecutor);

            return objectCompletableFuture.join();

        } else {
            threadExecutor.submit(asyncExecutorCommand::get);
            return null;
        }
    }
}
