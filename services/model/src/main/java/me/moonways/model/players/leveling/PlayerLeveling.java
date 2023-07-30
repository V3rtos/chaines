package me.moonways.model.players.leveling;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PlayerLeveling extends Remote {

    int calculateLevel(int totalExperience) throws RemoteException;

    int calculateTotalExperience(int level) throws RemoteException;

    int calculateExperienceToNextLevel(int level) throws RemoteException;

    int calculateExperiencePercentToNextLevel(int totalExperience) throws RemoteException;

    int subtotal(int level, int experience) throws RemoteException;
}
