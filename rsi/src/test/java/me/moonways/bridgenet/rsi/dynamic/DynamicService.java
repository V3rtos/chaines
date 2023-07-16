package me.moonways.bridgenet.rsi.dynamic;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;

public interface DynamicService extends RemoteService {

    IDynamicEmulator getEmulator() throws RemoteException;
}
