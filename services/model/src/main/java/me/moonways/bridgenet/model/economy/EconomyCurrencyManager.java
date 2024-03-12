package me.moonways.bridgenet.model.economy;

import me.moonways.bridgenet.model.economy.bank.BankTransaction;
import me.moonways.bridgenet.model.economy.bank.CurrencyBank;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface EconomyCurrencyManager extends Remote {

    BankTransaction executeEcho() throws RemoteException;

    BankTransaction executePay(int sum) throws RemoteException;

    BankTransaction executeCharge(int sum) throws RemoteException;

    BankTransaction executeTransfer(UUID receiverId, int sum) throws RemoteException;
}
