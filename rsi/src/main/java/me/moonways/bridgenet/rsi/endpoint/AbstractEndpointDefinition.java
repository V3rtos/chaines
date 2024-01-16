package me.moonways.bridgenet.rsi.endpoint;

import lombok.AccessLevel;
import lombok.Getter;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class AbstractEndpointDefinition extends UnicastRemoteObject {

    private static final long serialVersionUID = -1564328046265570096L;

    @Inject
    @Getter(AccessLevel.PROTECTED)
    private DependencyInjection injector;

    public AbstractEndpointDefinition() throws RemoteException {
        super();
    }
}
