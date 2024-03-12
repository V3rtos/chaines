package me.moonways.bridgenet.model.economy.bank;

import me.moonways.bridgenet.model.economy.Currency;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface CurrencyBank extends Remote {

    Currency getCurrency() throws RemoteException;

    BankTransaction echo(UUID playerId) throws RemoteException;

    BankTransaction pay(UUID playerId, int sum) throws RemoteException;

    BankTransaction charge(UUID playerId, int sum) throws RemoteException;

    BankTransaction transfer(UUID senderId, UUID receiverId, int sum) throws RemoteException;
}
