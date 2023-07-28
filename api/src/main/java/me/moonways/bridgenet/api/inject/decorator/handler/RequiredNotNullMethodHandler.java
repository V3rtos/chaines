package me.moonways.bridgenet.api.inject.decorator.handler;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;
import me.moonways.bridgenet.api.inject.decorator.DecoratedMethodHandler;
import me.moonways.bridgenet.api.inject.decorator.definition.RequiredNotNull;

@Log4j2
public class RequiredNotNullMethodHandler implements DecoratedMethodHandler {

    @Override
    public Object handleProxyInvocation(DecoratorInvocation invocation) {
        RequiredNotNull annotation = invocation.findAnnotation(RequiredNotNull.class);
        Object value = invocation.proceed();

        log.info("§3Decorated method {} matches null returned object", invocation);

        if (value == null) {
            NullPointerException nullPointerException = new NullPointerException(annotation.message());
            log.error("§4Decorator @RequiredNotNull has received null for {}", invocation);

            if (annotation.printStackTrace()) {
                nullPointerException.printStackTrace();
            }

            return null;
        }

        return value;
    }
}
