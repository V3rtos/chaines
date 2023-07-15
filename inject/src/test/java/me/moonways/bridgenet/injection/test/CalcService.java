package me.moonways.bridgenet.injection.test;

import me.moonways.bridgenet.injection.Component;

@Component
public class CalcService {

    public int sum(int a, int b) {
        return a + b;
    }
}
