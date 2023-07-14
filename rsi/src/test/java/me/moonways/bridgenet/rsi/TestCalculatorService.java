package me.moonways.bridgenet.rsi;

import me.moonways.bridgenet.rsi.service.RemoteService;

public interface TestCalculatorService extends RemoteService {

    int sum(int a, int b);

    long sum(long a, long b);

    int multiply(int a, int b);

    long multiply(long a, long b);
}
