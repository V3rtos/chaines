package me.moonways.endpoint.commands;

import me.moonways.bridgenet.model.service.commands.BridgenetCommand;
import me.moonways.bridgenet.model.service.commands.CommandsServiceModel;
import me.moonways.bridgenet.rmi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;

public final class CommandsServiceEndpoint extends EndpointRemoteObject implements CommandsServiceModel {
    private static final long serialVersionUID = 5500992295814900610L;

    public CommandsServiceEndpoint() throws RemoteException {
        super();
    }

    @Override
    public void register(BridgenetCommand bridgenetCommand) throws RemoteException {

    }
}
