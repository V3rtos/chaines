package me.moonways.bridgenet.injection.test;

import me.moonways.bridgenet.injection.Inject;
import me.moonways.bridgenet.injection.PostFactoryMethod;

import java.util.HashSet;
import java.util.Set;

public class CalculateEmulator {

    private final Set<Integer> storedResults = new HashSet<>();

    @Inject
    private CalcService calcService;

    public void sum(int a, int b) {
        int sum = calcService.sum(a, b);
        storedResults.add(sum);
    }

    public int[] getStoredResults() {
        return storedResults.stream().mapToInt(value -> value).toArray();
    }

    public boolean clearResults() {
        int size = storedResults.size();
        storedResults.clear();

        return size > 0;
    }
}
