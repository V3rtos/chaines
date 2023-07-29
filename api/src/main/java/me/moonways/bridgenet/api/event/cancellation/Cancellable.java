package me.moonways.bridgenet.api.event.cancellation;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Cancellable extends Remote {

    boolean isCancelled() throws RemoteException;

    void makeCancelled() throws RemoteException;

    void makeNotCancelled() throws RemoteException;
}
