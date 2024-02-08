package me.moonways.bridgenet.api.inject.decorator.persistence.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;
import me.moonways.bridgenet.api.inject.decorator.DecoratedMethodHandler;
import me.moonways.bridgenet.api.inject.decorator.persistence.RequiredNotNull;

@Log4j2
public class RequiredNotNullMethodHandler implements DecoratedMethodHandler {

    @Override
    public Object handleProxyInvocation(DecoratorInvocation invocation) {
        RequiredNotNull annotation = invocation.findAnnotation(RequiredNotNull.class);

        log.info("§3Decorated method {} has matching on NullPointerException", invocation);
        Object value = invocation.proceed();

        if (value == null) {
            throw new NullPointerException(annotation.message());
        }

        return value;
    }
}
