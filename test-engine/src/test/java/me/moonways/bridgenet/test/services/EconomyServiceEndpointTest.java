package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.economy.Currency;
import me.moonways.bridgenet.model.economy.EconomyCurrencyManager;
import me.moonways.bridgenet.model.economy.EconomyServiceModel;
import me.moonways.bridgenet.model.economy.bank.BankTransaction;
import me.moonways.bridgenet.model.economy.bank.BankTransactionResult;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class EconomyServiceEndpointTest {

    private static final UUID PLAYER_ID = UUID.randomUUID();
    private static final Currency CURRENCY = Currency.COINS;

    @Inject
    private EconomyServiceModel serviceModel;

    @Test
    @Order(0)
    public void test_executeCharge() throws RemoteException {
        EconomyCurrencyManager economyCurrencyManager = serviceModel.getManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = economyCurrencyManager.executeCharge(2000);

        assertEquals(BankTransactionResult.SUCCESS_OPERATION, transaction.getResult());
        assertEquals(CURRENCY, transaction.getState().getCurrency());
        assertEquals(2000, transaction.getState().getReceived());
        assertEquals(0, transaction.getState().getSpent());
        assertEquals(2000, transaction.getState().getCurrent());
    }

    @Test
    @Order(1)
    public void test_executePay() throws RemoteException {
        EconomyCurrencyManager economyCurrencyManager = serviceModel.getManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = economyCurrencyManager.executePay(100);

        assertEquals(BankTransactionResult.SUCCESS_OPERATION, transaction.getResult());
        assertEquals(CURRENCY, transaction.getState().getCurrency());
        assertEquals(100, transaction.getState().getReceived());
        assertEquals(0, transaction.getState().getSpent());
        assertEquals(1900, transaction.getState().getCurrent());
    }

    @Test
    @Order(2)
    public void test_executeTransfer() throws RemoteException {
        EconomyCurrencyManager economyCurrencyManager = serviceModel.getManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = economyCurrencyManager.executeTransfer(UUID.randomUUID(), 990);

        assertEquals(BankTransactionResult.SUCCESS_OPERATION, transaction.getResult());
        assertEquals(CURRENCY, transaction.getState().getCurrency());
        assertEquals(0, transaction.getState().getReceived());
        assertEquals(990, transaction.getState().getSpent());
        assertEquals(910, transaction.getState().getCurrent());
    }

    @Test
    @Order(3)
    public void test_executeEcho() throws RemoteException {
        EconomyCurrencyManager economyCurrencyManager = serviceModel.getManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = economyCurrencyManager.executeEcho();

        assertEquals(BankTransactionResult.SUCCESS_OPERATION, transaction.getResult());
        assertEquals(CURRENCY, transaction.getState().getCurrency());
        assertEquals(0, transaction.getState().getReceived());
        assertEquals(0, transaction.getState().getSpent());
        assertEquals(910, transaction.getState().getCurrent());
    }
}
