package me.moonways.bridgenet.model.service.commands.label;

import me.moonways.bridgenet.model.service.commands.arg.Args;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Optional;

public interface CommandLabel extends Remote {

    String getValue() throws RemoteException;

    String equals(String value) throws RemoteException;

    String equalsIgnoreCase(String value) throws RemoteException;

    String toUpper() throws RemoteException;

    String toLower() throws RemoteException;

    Optional<Args> getArgs() throws RemoteException;

    int getLength() throws RemoteException;

    String trim() throws RemoteException;

    char[] toCharArray() throws RemoteException;

    boolean isEmpty() throws RemoteException;
}
