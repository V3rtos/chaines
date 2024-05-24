package me.moonways.bridgenet.model.service.economy.credit;

import me.moonways.bridgenet.model.service.economy.credit.rating.CreditRatingManager;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Collection;
import java.util.UUID;

public interface EconomyCreditManager extends Remote {

    CreditRatingManager getRatingManager() throws RemoteException;

    Collection<ActiveCredit> getActiveCredits() throws RemoteException;

    CreditOperation executeCreditGet(int amount, CreditSource source) throws RemoteException;

    CreditOperation executeCreditPayment(int amount) throws RemoteException;
}
