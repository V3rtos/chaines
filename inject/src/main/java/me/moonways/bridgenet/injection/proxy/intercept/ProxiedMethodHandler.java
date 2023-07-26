package me.moonways.bridgenet.injection.proxy.intercept;

import me.moonways.bridgenet.api.intercept.ProxiedMethod;

public interface ProxiedMethodHandler {

    Object handleProxyInvocation(ProxiedMethod proxiedMethod, Object[] args);
}
