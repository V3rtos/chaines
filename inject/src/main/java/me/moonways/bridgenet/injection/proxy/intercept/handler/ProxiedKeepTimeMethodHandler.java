package me.moonways.bridgenet.injection.proxy.intercept.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.injection.proxy.intercept.ProxiedMethod;
import me.moonways.bridgenet.injection.proxy.intercept.ProxiedMethodHandler;

@Log4j2
public class ProxiedKeepTimeMethodHandler implements ProxiedMethodHandler {

    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        long startTimeMillis = System.currentTimeMillis();
        Object returnObject = proxiedMethod.call(args);

        long keepTimeMillis = System.currentTimeMillis() - startTimeMillis;

        log.info("§3Proxied method {} proceed for {} ms.", proxiedMethod, keepTimeMillis);
        return returnObject;
    }
}
