package me.moonways.bridgenet.test.services;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.service.economy.EconomyServiceModel;
import me.moonways.bridgenet.model.service.economy.credit.EconomyCreditManager;
import me.moonways.bridgenet.model.service.economy.currency.EconomyCurrencyManager;
import me.moonways.bridgenet.model.service.economy.currency.bank.BankTransaction;
import me.moonways.bridgenet.model.service.economy.deposit.ActiveDeposit;
import me.moonways.bridgenet.model.service.economy.deposit.DepositOperation;
import me.moonways.bridgenet.model.service.economy.deposit.EconomyDepositManager;
import me.moonways.bridgenet.test.data.TestConst;
import me.moonways.bridgenet.test.data.junit.assertion.ServicesAssert;
import me.moonways.bridgenet.test.engine.ModernTestEngineRunner;
import me.moonways.bridgenet.test.engine.module.impl.RmiServicesModule;
import me.moonways.bridgenet.test.engine.persistance.TestModules;
import me.moonways.bridgenet.test.engine.persistance.TestOrdered;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

@RunWith(ModernTestEngineRunner.class)
@TestModules(RmiServicesModule.class)
public class EconomyServiceEndpointTest {

    @Inject
    private EconomyServiceModel serviceModel;

    @Test
    @TestOrdered(1)
    public void test_bankExecuteCharge() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        BankTransaction transaction = currencyManager.executeCharge(TestConst.Economy.BANK_CHARGE_AMOUNT);
        ServicesAssert.assertBankTransaction(transaction, TestConst.Economy.BANK_CHARGE_AMOUNT, 0, TestConst.Economy.BANK_CHARGE_AMOUNT);
    }

    @Test
    @TestOrdered(2)
    public void test_bankExecutePay() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        BankTransaction transaction = currencyManager.executePay(TestConst.Economy.BANK_PAY_AMOUNT);

        ServicesAssert.assertBankTransaction(transaction, 0, TestConst.Economy.BANK_PAY_AMOUNT,
                (TestConst.Economy.BANK_CHARGE_AMOUNT - TestConst.Economy.BANK_PAY_AMOUNT));
    }

    @Test
    @TestOrdered(3)
    public void test_bankExecuteTransfer() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        BankTransaction transaction = currencyManager.executeTransfer(UUID.randomUUID(), TestConst.Economy.BANK_TRANSFER_AMOUNT);
        ServicesAssert.assertBankTransaction(transaction, 0, TestConst.Economy.BANK_TRANSFER_AMOUNT, 910);
    }

    @Test
    @TestOrdered(4)
    public void test_bankExecuteEcho() throws RemoteException {
        EconomyCurrencyManager currencyManager = serviceModel.getCurrencyManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        BankTransaction transaction = currencyManager.executeEcho();
        ServicesAssert.assertBankTransaction(transaction, 0, 0, 910);
    }

    @Test
    @TestOrdered(5)
    public void test_depositOpen() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        DepositOperation depositOperation = depositManager.openDeposit();

        ActiveDeposit deposit = depositOperation.getDeposit();
    }

    @Test
    @TestOrdered(6)
    public void test_depositsGet() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        Collection<ActiveDeposit> activeDeposits = depositManager.getActiveDeposits();
        assertEquals(1, activeDeposits.size());
    }

    @Test
    @TestOrdered(7)
    public void test_depositInvest() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        ActiveDeposit deposit = depositManager.getActiveDeposits().get(0);
        DepositOperation depositOperation = deposit.invest(TestConst.Economy.DEPOSIT_INVEST_AMOUNT);
    }

    @Test
    @TestOrdered(8)
    public void test_depositWithdraw() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        ActiveDeposit deposit = depositManager.getActiveDeposits().get(0);
        DepositOperation depositOperation = deposit.withdraw(TestConst.Economy.DEPOSIT_WITHDRAW_AMOUNT);
    }

    @Test
    @TestOrdered(9)
    public void test_depositClose() throws RemoteException {
        EconomyDepositManager depositManager = serviceModel.getDepositManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);

        ActiveDeposit deposit = depositManager.getActiveDeposits().get(0);
        DepositOperation depositOperation = depositManager.closeDeposit(deposit.getDepositId());
    }

    @Test
    @TestOrdered(10)
    public void test_creditGet() throws RemoteException {
        EconomyCreditManager creditManager = serviceModel.getCreditManager(
                TestConst.Player.ID,
                TestConst.Economy.CURRENCY);
    }
}
