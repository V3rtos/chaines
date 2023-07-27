package me.moonways.bridgenet.api.inject.factory;

@FunctionalInterface
public interface ObjectFactory {

    <T> T create(Class<T> cls);
}
