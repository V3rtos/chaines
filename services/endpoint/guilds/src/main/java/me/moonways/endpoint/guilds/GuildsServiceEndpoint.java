package me.moonways.endpoint.guilds;

import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.bridgenet.model.service.guilds.GuildsServiceModel;

import java.rmi.RemoteException;

public final class GuildsServiceEndpoint extends EndpointRemoteObject implements GuildsServiceModel {

    private static final long serialVersionUID = -7738118810625529481L;

    public GuildsServiceEndpoint() throws RemoteException {
        super();
    }
}
