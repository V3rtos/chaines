package me.moonways.bridgenet.model.service.players.component;

import me.moonways.bridgenet.model.service.economy.credit.EconomyCreditManager;
import me.moonways.bridgenet.model.service.economy.currency.Currency;
import me.moonways.bridgenet.model.service.economy.currency.EconomyCurrencyManager;
import me.moonways.bridgenet.model.service.economy.deposit.EconomyDepositManager;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerEconomy extends Remote {

    Currency getType() throws RemoteException;

    EconomyCurrencyManager getCurrency() throws RemoteException;

    EconomyCreditManager getCredits() throws RemoteException;

    EconomyDepositManager getDeposits() throws RemoteException;
}
