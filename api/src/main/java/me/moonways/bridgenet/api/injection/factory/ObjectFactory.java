package me.moonways.bridgenet.api.injection.factory;

@FunctionalInterface
public interface ObjectFactory {

    <T> T create(Class<T> cls);
}
