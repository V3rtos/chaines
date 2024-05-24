package me.moonways.bridgenet.model.service.economy.deposit;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

public interface EconomyDepositManager extends Remote {

    List<ActiveDeposit> getActiveDeposits() throws RemoteException;

    DepositOperation openDeposit() throws RemoteException;

    DepositOperation closeDeposit(UUID depositId) throws RemoteException;

    DepositOperation invest(UUID depositId, int sum) throws RemoteException;

    DepositOperation withdraw(UUID depositId, int sum) throws RemoteException;
}
