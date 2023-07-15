package me.moonways.bridgenet.injection.factory;

@FunctionalInterface
public interface ObjectFactory {

    <T> T create(Class<T> cls);
}
