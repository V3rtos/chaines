package me.moonways.bridgenet.model.economy;

import me.moonways.bridgenet.model.economy.credit.EconomyCreditManager;
import me.moonways.bridgenet.model.economy.currency.EconomyCurrencyManager;
import me.moonways.bridgenet.model.economy.currency.bank.CurrencyBank;
import me.moonways.bridgenet.model.economy.currency.Currency;
import me.moonways.bridgenet.model.economy.deposit.EconomyDepositManager;
import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;
import java.util.UUID;

public interface EconomyServiceModel extends RemoteService {

    /**
     * Создать новый банк по управлению указанной валютой.
     * @param currency - валюта.
     */
    CurrencyBank createBank(Currency currency) throws RemoteException;

    /**
     * Получить управленческий объект над операциями пользователя.
     *
     * @param playerId - уникальный идентификатор пользователя.
     * @param currency - валюта, которой необходимо управлять.
     */
    EconomyCurrencyManager getCurrencyManager(UUID playerId, Currency currency) throws RemoteException;

    /**
     * Получить управленческий объект над операциями пользователя.
     *
     * @param playerName - имя пользователя.
     * @param currency - валюта, которой необходимо управлять.
     */
    EconomyCurrencyManager getCurrencyManager(String playerName, Currency currency) throws RemoteException;

    EconomyDepositManager getDepositManager(UUID playerId, Currency currency) throws RemoteException;

    EconomyDepositManager getDepositManager(String playerName, Currency currency) throws RemoteException;

    EconomyCreditManager getCreditManager(UUID playerId, Currency currency) throws RemoteException;

    EconomyCreditManager getCreditManager(String playerName, Currency currency) throws RemoteException;
}
