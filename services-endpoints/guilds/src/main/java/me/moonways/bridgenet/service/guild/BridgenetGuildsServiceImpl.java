package me.moonways.bridgenet.service.guild;

import me.moonways.bridgenet.injection.Component;
import me.moonways.service.api.guilds.BridgenetGuildsService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

@Component
public final class BridgenetGuildsServiceImpl extends UnicastRemoteObject implements BridgenetGuildsService {

    private static final long serialVersionUID = -7738118810625529481L;

    public BridgenetGuildsServiceImpl() throws RemoteException {
        super();
    }
}
