package me.moonways.bridgenet.rsi.endpoint;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public abstract class AbstractEndpointDefinition extends UnicastRemoteObject {

    private static final long serialVersionUID = -1564328046265570096L;

    public AbstractEndpointDefinition() throws RemoteException {
        super();
    }
}
