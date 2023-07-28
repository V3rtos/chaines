package me.moonways.bridgenet.api.inject.decorator.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;
import me.moonways.bridgenet.api.inject.decorator.DecoratedMethodHandler;

@Log4j2
public class KeepTimeMethodHandler implements DecoratedMethodHandler {

    @Override
    public Object handleProxyInvocation(DecoratorInvocation invocation) {
        long startTimeMillis = System.currentTimeMillis();
        long startTimeNanos = System.nanoTime();

        Object returnObject = invocation.proceed();

        long keepTimeMillis = System.currentTimeMillis() - startTimeMillis;
        long keepTimeNanos = System.nanoTime() - startTimeNanos;

        log.info("§3Decorated method {} proceed for {} ms. ({} ns.)", invocation, keepTimeMillis, keepTimeNanos);
        return returnObject;
    }
}
