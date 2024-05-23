package me.moonways.bridgenet.model.commands;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommandServiceModel extends RemoteService, Remote {

    void register(BridgenetCommand bridgenetCommand) throws RemoteException;
}
