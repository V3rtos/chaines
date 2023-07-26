package me.moonways.bridgenet.injection.proxy.intercept.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.intercept.ProxiedMethod;
import me.moonways.bridgenet.injection.proxy.intercept.ProxiedMethodHandler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

@Log4j2
public class ProxiedAsyncMethodHandler implements ProxiedMethodHandler {

    private static final ExecutorService ASYNC_POOL_EXECUTOR
            = Executors.newCachedThreadPool();

    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        Supplier<Object> asyncExecutorCommand = () -> {

            log.info("§3Running Proxied method {} asynchronous execution: [thread={}]", proxiedMethod, Thread.currentThread().getName());
            return proxiedMethod.call(args);
        };

        if (!proxiedMethod.isVoid()) {
            CompletableFuture<Object> objectCompletableFuture
                    = CompletableFuture.supplyAsync(asyncExecutorCommand, ASYNC_POOL_EXECUTOR);

            return objectCompletableFuture.join();

        } else {

            ASYNC_POOL_EXECUTOR.execute(asyncExecutorCommand::get);
            return null;
        }
    }
}
