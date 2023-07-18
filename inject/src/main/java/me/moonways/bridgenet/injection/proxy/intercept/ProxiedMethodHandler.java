package me.moonways.bridgenet.injection.proxy.intercept;

public interface ProxiedMethodHandler {

    Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args);
}
