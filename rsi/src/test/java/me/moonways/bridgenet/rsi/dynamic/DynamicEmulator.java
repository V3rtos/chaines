package me.moonways.bridgenet.rsi.dynamic;

import java.io.Serializable;

public class DynamicEmulator implements Serializable, IDynamicEmulator {

    private static final long serialVersionUID = 7663039635079251376L;

    public void sayHello() {
        System.out.println("Hello world!");
    }
}
