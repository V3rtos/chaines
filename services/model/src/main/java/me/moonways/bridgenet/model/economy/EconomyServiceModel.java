package me.moonways.bridgenet.model.economy;

import me.moonways.bridgenet.model.economy.bank.CurrencyBank;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.UUID;

public interface EconomyServiceModel extends RemoteService {

    CurrencyBank createBank(Currency currency) throws RemoteException;

    EconomyCurrencyManager getManager(UUID playerId, Currency currency) throws RemoteException;

    EconomyCurrencyManager getManager(String playerName, Currency currency) throws RemoteException;
}
