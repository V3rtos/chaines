package me.moonways.endpoint.economy.currency;

import lombok.Getter;
import me.moonways.bridgenet.model.economy.currency.Currency;
import me.moonways.bridgenet.model.economy.currency.bank.BankTransaction;
import me.moonways.bridgenet.model.economy.currency.bank.CurrencyBank;
import me.moonways.bridgenet.rsi.endpoint.persistance.EndpointRemoteObject;
import me.moonways.endpoint.economy.db.EconomyCurrencyDbRepository;

import java.rmi.RemoteException;
import java.util.UUID;

public class CurrencyBankStub extends EndpointRemoteObject implements CurrencyBank {
    private static final long serialVersionUID = -7425948721433814158L;

    @Getter
    private final Currency currency;

    private final EconomyCurrencyDbRepository dbRepository;

    public CurrencyBankStub(Currency currency, EconomyCurrencyDbRepository dbRepository) throws RemoteException {
        super();
        this.currency = currency;
        this.dbRepository = dbRepository;
    }

    private BankTransaction createTransactionAsNotFound() {
        return BankTransaction.builder()
                .state(BankTransaction.PostTransferState.builder()
                        .currency(getCurrency())
                        .spent(0)
                        .received(0)
                        .current(0)
                        .build())
                .transactionId(UUID.randomUUID())
                .result(BankTransaction.Result.NOT_FOUND)
                .build();
    }

    @Override
    public BankTransaction echo(UUID playerId) throws RemoteException {
        int value = dbRepository.getValue(playerId, currency);
        if (value == -1) {
            return createTransactionAsNotFound();
        }
        return BankTransaction.builder()
                .state(BankTransaction.PostTransferState.builder()
                        .currency(getCurrency())
                        .spent(0)
                        .received(0)
                        .current(value)
                        .build())
                .transactionId(UUID.randomUUID())
                .result(BankTransaction.Result.SUCCESS_OPERATION)
                .build();
    }

    @Override
    public BankTransaction pay(UUID playerId, int sum) throws RemoteException {
        int value = dbRepository.getValue(playerId, currency);
        if (value == -1) {
            return createTransactionAsNotFound();
        }

        boolean hasFunds = value >= sum;

        if (hasFunds) {
            dbRepository.updateEntity(playerId, currency, value - sum);
        }

        return BankTransaction.builder()
                .state(BankTransaction.PostTransferState.builder()
                        .currency(getCurrency())
                        .spent(hasFunds ? sum : 0)
                        .received(0)
                        .current(hasFunds ? value - sum : value)
                        .build())
                .transactionId(UUID.randomUUID())
                .result(hasFunds ? BankTransaction.Result.SUCCESS_OPERATION : BankTransaction.Result.NOT_ENOUGH)
                .build();
    }

    @Override
    public BankTransaction charge(UUID playerId, int sum) throws RemoteException {
        int value = dbRepository.getValue(playerId, currency);
        if (value == -1) {
            return createTransactionAsNotFound();
        }

        dbRepository.updateEntity(playerId, currency, value + sum);
        return BankTransaction.builder()
                .state(BankTransaction.PostTransferState.builder()
                        .currency(getCurrency())
                        .spent(0)
                        .received(sum)
                        .current(value + sum)
                        .build())
                .transactionId(UUID.randomUUID())
                .result(BankTransaction.Result.SUCCESS_OPERATION)
                .build();
    }

    @Override
    public BankTransaction transfer(UUID senderId, UUID receiverId, int sum) throws RemoteException {
        charge(receiverId, sum);
        return pay(senderId, sum);
    }
}
