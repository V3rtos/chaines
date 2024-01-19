package me.moonways.endpoint.bus;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.bridgenet.model.bus.BusServiceModel;

import java.rmi.RemoteException;

public class BusServiceEndpoint extends AbstractEndpointDefinition implements BusServiceModel {

    private static final long serialVersionUID = 3915937249408474506L;

    public BusServiceEndpoint() throws RemoteException {
        super();
    }
}
