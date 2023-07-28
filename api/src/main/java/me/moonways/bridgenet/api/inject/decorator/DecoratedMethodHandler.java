package me.moonways.bridgenet.api.inject.decorator;

import me.moonways.bridgenet.api.inject.decorator.DecoratorInvocation;

public interface DecoratedMethodHandler {

    Object handleProxyInvocation(DecoratorInvocation invocation);
}
