package me.moonways.bridgenet.model.reports;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Report extends Remote {

    ReportReason getReason() throws RemoteException;

    String getWhoReportedName() throws RemoteException;

    String getIntruderName() throws RemoteException;

    String getComment() throws RemoteException;

    String getServerName() throws RemoteException;

    long getCreatedTimeMillis() throws RemoteException;
}
