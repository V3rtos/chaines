package me.moonways.bridgenet.rsi;

import me.moonways.bridgenet.rsi.service.RemoteService;

import java.rmi.RemoteException;

public interface TestCalculatorService extends RemoteService {

    int sum(int a, int b) throws RemoteException;

    long sum(long a, long b) throws RemoteException;

    int multiply(int a, int b) throws RemoteException;

    long multiply(long a, long b) throws RemoteException;
}
