package me.moonways.bridgenet.rsi.calc;

public class TestCalculatorServiceObject implements TestCalculatorService {

    @Override
    public int sum(int a, int b) {
        return a + b;
    }

    @Override
    public long sum(long a, long b) {
        return a + b;
    }

    @Override
    public int multiply(int a, int b) {
        return a * b;
    }

    @Override
    public long multiply(long a, long b) {
        return a * b;
    }
}
