package me.moonways.bridgenet.api.inject.decorator.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.proxy.DecoratedMethodHandler;
import me.moonways.bridgenet.api.inject.decorator.definition.RequiredNotNull;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@Log4j2
public class RequiredNotNullMethodHandler implements DecoratedMethodHandler {

    @Override
    public Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args) {
        if (proxiedMethod.isVoid()) {
            log.info("§4Cannot invoke @RequiredNotNull decorator for {}: §cConflict with VOID return-type", proxiedMethod);

            return proxiedMethod.call(args);
        }

        RequiredNotNull annotation = proxiedMethod.findAnnotation(RequiredNotNull.class);
        Object value = proxiedMethod.call(args);

        log.info("§3Decorated method {} matches null returned object", proxiedMethod);

        if (value == null) {
            NullPointerException nullPointerException = new NullPointerException(annotation.message());
            log.error("§4Decorator @RequiredNotNull has received null for {}", proxiedMethod);

            if (annotation.printStackTrace()) {
                nullPointerException.printStackTrace();
            }

            return null;
        }

        return value;
    }
}
