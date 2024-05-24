package me.moonways.endpoint.players.player;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.model.service.economy.credit.EconomyCreditManager;
import me.moonways.bridgenet.model.service.economy.currency.Currency;
import me.moonways.bridgenet.model.service.economy.currency.EconomyCurrencyManager;
import me.moonways.bridgenet.model.service.economy.deposit.EconomyDepositManager;
import me.moonways.bridgenet.model.service.players.component.PlayerEconomy;

import java.rmi.RemoteException;

@RequiredArgsConstructor
public class PlayerEconomyStub implements PlayerEconomy {

    private final Currency currency;

    private final EconomyCurrencyManager currencyManager;
    private final EconomyCreditManager creditManager;
    private final EconomyDepositManager depositManager;

    @Override
    public Currency getType() throws RemoteException {
        return currency;
    }

    @Override
    public EconomyCurrencyManager getCurrency() throws RemoteException {
        return currencyManager;
    }

    @Override
    public EconomyCreditManager getCredits() throws RemoteException {
        return creditManager;
    }

    @Override
    public EconomyDepositManager getDeposits() throws RemoteException {
        return depositManager;
    }
}
