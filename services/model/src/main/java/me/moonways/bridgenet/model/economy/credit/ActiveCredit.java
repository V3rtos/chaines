package me.moonways.bridgenet.model.economy.credit;

import me.moonways.bridgenet.model.economy.credit.payments.CreditScheduledPayments;
import me.moonways.bridgenet.model.economy.currency.Currency;
import me.moonways.bridgenet.model.economy.credit.payments.CreditManualPayments;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.UUID;

public interface ActiveCredit extends Remote {

    UUID getPlayerId() throws RemoteException;

    UUID getCreditId() throws RemoteException;

    Currency getCurrency() throws RemoteException;

    CreditSource getSource() throws RemoteException;

    CreditManualPayments getManualPayments() throws RemoteException;

    CreditScheduledPayments getScheduledPayments() throws RemoteException;

    int getAmount() throws RemoteException;

    double getStake() throws RemoteException;
}
