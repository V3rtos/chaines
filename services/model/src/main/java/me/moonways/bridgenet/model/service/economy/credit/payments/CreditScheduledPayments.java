package me.moonways.bridgenet.model.service.economy.credit.payments;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.time.Period;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface CreditScheduledPayments extends Remote {

    Date getWhenPassed() throws RemoteException;

    Period getPaymentFrequency() throws RemoteException;

    int getRateLoopsCount() throws RemoteException;

    int getPaymentAmount() throws RemoteException;

    double getFirstPaymentDiscount() throws RemoteException;
}
