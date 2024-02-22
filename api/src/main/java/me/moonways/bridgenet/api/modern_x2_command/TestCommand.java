package me.moonways.bridgenet.api.modern_x2_command;

@InjectCommand("test")
public class TestCommand {

    @General
    public void general(ExecutionContext executionContext) {
    }

    @Sub({"sub first", "sub one"})
    public void sub_test_first(ExecutionContext executionContext) {
    }

    @Sub({"sub seconds", "sub two"})
    public void sub_test_second(ExecutionContext executionContext) {
    }
}
