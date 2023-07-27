package me.moonways.bridgenet.api.inject.decorator.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.proxy.DecoratedMethodHandler;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@Log4j2
public class KeepTimeMethodHandler implements DecoratedMethodHandler {

    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        long startTimeMillis = System.currentTimeMillis();
        long startTimeNanos = System.nanoTime();

        Object returnObject = proxiedMethod.call(args);

        long keepTimeMillis = System.currentTimeMillis() - startTimeMillis;
        long keepTimeNanos = System.nanoTime() - startTimeNanos;

        log.info("§3Decorated method {} proceed for {} ms. ({} ns.)", proxiedMethod, keepTimeMillis, keepTimeNanos);
        return returnObject;
    }
}
