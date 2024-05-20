package me.moonways.bridgenet.model.commands;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface CommandExecutor extends Remote {

    CommandResult execute(Command command, CommandReader reader) throws RemoteException;
}
