package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.economy.credit.EconomyCreditManager;
import me.moonways.bridgenet.model.economy.currency.Currency;
import me.moonways.bridgenet.model.economy.currency.EconomyCurrencyManager;
import me.moonways.bridgenet.model.economy.EconomyServiceModel;
import me.moonways.bridgenet.model.economy.currency.bank.BankTransaction;
import me.moonways.bridgenet.model.economy.deposit.ActiveDeposit;
import me.moonways.bridgenet.model.economy.deposit.DepositOperation;
import me.moonways.bridgenet.model.economy.deposit.EconomyDepositManager;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import me.moonways.bridgenet.test.engine.persistance.Order;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

@RunWith(BridgenetJUnitTestRunner.class)
public class EconomyServiceEndpointTest {

    private static final UUID PLAYER_ID = UUID.randomUUID();
    private static final Currency CURRENCY = Currency.COINS;

    private static final int BANK_CHARGE_AMOUNT = 2000;
    private static final int BANK_PAY_AMOUNT = 100;
    private static final int BANK_TRANSFER_AMOUNT = 990;

    private static final int DEPOSIT_INVEST_AMOUNT = 62_000;
    private static final int DEPOSIT_WITHDRAW_AMOUNT = 20_000;

    @Inject
    private EconomyServiceModel serviceModel;

    private void assertBankTransaction(BankTransaction transaction, int receivedExpect, int spentExpect, int currentExpect) {
        assertEquals(BankTransaction.Result.SUCCESS_OPERATION, transaction.getResult());
        assertEquals(CURRENCY, transaction.getState().getCurrency());
        assertEquals(receivedExpect, transaction.getState().getReceived());
        assertEquals(spentExpect, transaction.getState().getSpent());
        assertEquals(currentExpect, transaction.getState().getCurrent());
    }

    @Test
    @Order(0)
    public void test_bankExecuteCharge() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = currencyManager.executeCharge(BANK_CHARGE_AMOUNT);

        assertBankTransaction(transaction, BANK_CHARGE_AMOUNT, 0, BANK_CHARGE_AMOUNT);
    }

    @Test
    @Order(1)
    public void test_bankExecutePay() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = currencyManager.executePay(BANK_PAY_AMOUNT);

        assertBankTransaction(transaction, 0, BANK_PAY_AMOUNT, (BANK_CHARGE_AMOUNT - BANK_PAY_AMOUNT));
    }

    @Test
    @Order(2)
    public void test_bankExecuteTransfer() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = currencyManager.executeTransfer(UUID.randomUUID(), BANK_TRANSFER_AMOUNT);

        assertBankTransaction(transaction, 0, BANK_TRANSFER_AMOUNT, 910);
    }

    @Test
    @Order(3)
    public void test_bankExecuteEcho() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(PLAYER_ID, CURRENCY);

        BankTransaction transaction = currencyManager.executeEcho();

        assertBankTransaction(transaction, 0, 0, 910);
    }

    @Test
    @Order(4)
    public void test_depositOpen() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(PLAYER_ID, CURRENCY);

        DepositOperation depositOperation = depositManager.openDeposit(PLAYER_ID);

        ActiveDeposit deposit = depositOperation.getDeposit();
    }

    @Test
    @Order(5)
    public void test_depositsGet() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(PLAYER_ID, CURRENCY);
        Collection<ActiveDeposit> activeDeposits = depositManager.getActiveDeposits(PLAYER_ID);

        assertEquals(1, activeDeposits.size());
    }

    @Test
    @Order(6)
    public void test_depositInvest() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(PLAYER_ID, CURRENCY);
        ActiveDeposit deposit = depositManager.getActiveDeposits(PLAYER_ID).stream().findFirst().orElse(null);

        DepositOperation depositOperation = depositManager.invest(deposit.getDepositId(), DEPOSIT_INVEST_AMOUNT);
    }

    @Test
    @Order(7)
    public void test_depositWithdraw() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(PLAYER_ID, CURRENCY);
        ActiveDeposit deposit = depositManager.getActiveDeposits(PLAYER_ID).stream().findFirst().orElse(null);

        DepositOperation depositOperation = depositManager.withdraw(deposit.getDepositId(), DEPOSIT_WITHDRAW_AMOUNT);
    }

    @Test
    @Order(8)
    public void test_depositClose() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(PLAYER_ID, CURRENCY);
        ActiveDeposit deposit = depositManager.getActiveDeposits(PLAYER_ID).stream().findFirst().orElse(null);

        DepositOperation depositOperation = depositManager.closeDeposit(deposit.getDepositId());
    }

    @Test
    @Order(9)
    public void test_creditGet() throws RemoteException {
        EconomyCreditManager creditManager = serviceModel.getCreditManager(PLAYER_ID, CURRENCY);
    }
}
