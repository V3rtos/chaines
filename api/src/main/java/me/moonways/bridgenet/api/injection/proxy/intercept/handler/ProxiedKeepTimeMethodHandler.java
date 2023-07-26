package me.moonways.bridgenet.api.injection.proxy.intercept.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.injection.proxy.intercept.ProxiedMethodHandler;
import me.moonways.bridgenet.api.intercept.ProxiedMethod;

@Log4j2
public class ProxiedKeepTimeMethodHandler implements ProxiedMethodHandler {

    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        long startTimeMillis = System.currentTimeMillis();
        long startTimeNanos = System.nanoTime();

        Object returnObject = proxiedMethod.call(args);

        long keepTimeMillis = System.currentTimeMillis() - startTimeMillis;
        long keepTimeNanos = System.nanoTime() - startTimeNanos;

        log.info("§3Proxied method {} proceed for {} ms. ({} ns.)", proxiedMethod, keepTimeMillis, keepTimeNanos);
        return returnObject;
    }
}
