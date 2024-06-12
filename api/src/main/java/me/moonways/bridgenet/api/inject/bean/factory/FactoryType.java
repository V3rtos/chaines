package me.moonways.bridgenet.api.inject.bean.factory;

import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.Singleton;
import me.moonways.bridgenet.api.inject.bean.factory.type.ConstructorFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.FactoryMethodFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.UnsafeFactory;

@ToString
@RequiredArgsConstructor
public enum FactoryType {

    UNSAFE(Singleton.of(new UnsafeFactory())),
    CONSTRUCTOR(Singleton.of(new ConstructorFactory())),
    METHOD(Singleton.of(new FactoryMethodFactory())),
    ;

    public static FactoryType DEFAULT;
    private final Singleton<BeanFactory> singleton;

    public BeanFactory get() {
        return singleton.get();
    }
}
