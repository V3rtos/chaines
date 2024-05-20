package me.moonways.bridgenet.model.commands;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RegistrationExecutor extends Remote {

    void register(Command command) throws RemoteException;
}
