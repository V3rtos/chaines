package me.moonways.bridgenet.injection.test;

import me.moonways.bridgenet.injection.DependencyInjection;

import java.util.Arrays;

public class TestApp {

    public static void main(String[] args) {
        DependencyInjection dependencyInjection = new DependencyInjection();
        dependencyInjection.bind(new CalcService());

        CalculateEmulator emulator = new CalculateEmulator();

        dependencyInjection.injectFields(emulator);

        emulator.sum(5, 2);
        System.out.println(Arrays.toString(emulator.getStoredResults()));
    }
}
