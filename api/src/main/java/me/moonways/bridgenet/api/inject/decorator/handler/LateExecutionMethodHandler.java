package me.moonways.bridgenet.api.inject.decorator.handler;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.definition.Async;
import me.moonways.bridgenet.api.inject.decorator.definition.LateExecution;
import me.moonways.bridgenet.api.inject.decorator.proxy.DecoratedMethodHandler;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@Log4j2
public class LateExecutionMethodHandler implements DecoratedMethodHandler {

    private static final ScheduledExecutorService SCHEDULER
        = Executors.newScheduledThreadPool(2);

    @SneakyThrows
    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        if (proxiedMethod.hasAnnotation(Async.class)) {
            log.error("§4Decorated method {} cannot invoke: §cFounded conflict @LateExecution with @Async", proxiedMethod);
            return null;
        }

        LateExecution annotation = proxiedMethod.findAnnotation(LateExecution.class);
        CompletableFuture<Object> completableFuture = new CompletableFuture<>();

        SCHEDULER.schedule((Runnable) () -> completableFuture.complete(proxiedMethod.call(args)), annotation.delay(), annotation.unit());
        log.info("§3Running decorated {} scheduled execution: [info={}]", proxiedMethod,
            annotation.toString().substring(annotation.annotationType().getName().length() + 1));

        if (proxiedMethod.isVoid())
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
