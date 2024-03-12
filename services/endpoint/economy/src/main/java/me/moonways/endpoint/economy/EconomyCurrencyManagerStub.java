package me.moonways.endpoint.economy;

import me.moonways.bridgenet.model.economy.EconomyCurrencyManager;
import me.moonways.bridgenet.model.economy.bank.BankTransaction;
import me.moonways.bridgenet.model.economy.bank.CurrencyBank;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;
import java.util.UUID;

// todo - оптимизировать, добавив кеширование данных
public class EconomyCurrencyManagerStub extends EndpointRemoteObject implements EconomyCurrencyManager {
    private static final long serialVersionUID = 2514616303549721781L;

    private final UUID playerId;
    private final CurrencyBank bank;

    public EconomyCurrencyManagerStub(UUID playerId, CurrencyBank bank) throws RemoteException {
        super();
        this.playerId = playerId;
        this.bank = bank;
    }

    @Override
    public BankTransaction executeEcho() throws RemoteException {
        return bank.echo(playerId);
    }

    @Override
    public BankTransaction executePay(int sum) throws RemoteException {
        return bank.pay(playerId, sum);
    }

    @Override
    public BankTransaction executeCharge(int sum) throws RemoteException {
        return bank.charge(playerId, sum);
    }

    @Override
    public BankTransaction executeTransfer(UUID receiverId, int sum) throws RemoteException {
        return bank.transfer(playerId, receiverId, sum);
    }
}
