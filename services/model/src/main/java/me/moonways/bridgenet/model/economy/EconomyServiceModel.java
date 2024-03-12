package me.moonways.bridgenet.model.economy;

import me.moonways.bridgenet.model.economy.bank.CurrencyBank;
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
    EconomyCurrencyManager getManager(UUID playerId, Currency currency) throws RemoteException;

    /**
     * Получить управленческий объект над операциями пользователя.
     *
     * @param playerName - имя пользователя.
     * @param currency - валюта, которой необходимо управлять.
     */
    EconomyCurrencyManager getManager(String playerName, Currency currency) throws RemoteException;
}
