package me.moonways.bridgenet.api.inject.decorator;

import java.util.function.Supplier;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Delegate;
import me.moonways.bridgenet.api.proxy.ProxiedMethod;

@RequiredArgsConstructor
public class DecoratorInvocation {

    @Delegate
    private final ProxiedMethod method;
    private final Object[] args;

    @Setter
    private Supplier<Object> invocationProcess;

    @Getter
    private boolean performed;

    public synchronized Object proceed() {
        if (invocationProcess == null) {
            invocationProcess = (this::callNative);
        }

        performed = true;
        return invocationProcess.get();
    }

    public synchronized Object callNative() {
        return method.call(args);
    }

    @Override
    public String toString() {
        return method.toString();
    }
}
