package me.moonways.endpoint.economy.currency;

import me.moonways.bridgenet.model.economy.currency.CurrencyOperationHistory;
import me.moonways.bridgenet.model.economy.currency.CurrencyOperationUnit;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class CurrencyOperationsHistory extends EndpointRemoteObject implements CurrencyOperationHistory {

    private final UUID playerId;

    public CurrencyOperationsHistory(UUID playerId) throws RemoteException {
        super();
        this.playerId = playerId;
    }

    @Override
    public Optional<CurrencyOperationUnit> getLast() throws RemoteException {
        return Optional.empty();
    }

    @Override
    public Collection<CurrencyOperationUnit> getUnitsOf(Date date) throws RemoteException {
        return null;
    }

    @Override
    public Optional<Long> getSumOf(Date date) throws RemoteException {
        return Optional.empty();
    }
}
