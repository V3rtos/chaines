package me.moonways.bridgenet.api.inject.decorator.proxy;

import me.moonways.bridgenet.api.proxy.ProxiedMethod;

public interface DecoratedMethodHandler {

    Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args);
}
