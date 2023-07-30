package me.moonways.endpoint.guilds;

import me.moonways.bridgenet.rsi.endpoint.AbstractEndpointDefinition;
import me.moonways.model.guilds.GuildsServiceModel;

import java.rmi.RemoteException;

public final class GuildsServiceEndpoint extends AbstractEndpointDefinition implements GuildsServiceModel {

    private static final long serialVersionUID = -7738118810625529481L;

    public GuildsServiceEndpoint() throws RemoteException {
        super();
    }
}
