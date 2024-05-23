package me.moonways.bridgenet.model.service.commands;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommandsServiceModel extends RemoteService, Remote {

    void register(BridgenetCommand bridgenetCommand) throws RemoteException;
}
