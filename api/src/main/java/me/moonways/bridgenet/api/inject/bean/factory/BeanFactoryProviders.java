package me.moonways.bridgenet.api.inject.bean.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.api.inject.bean.factory.type.ConstructorFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.FactoryMethodFactory;
import me.moonways.bridgenet.api.inject.bean.factory.type.UnsafeFactory;

@ToString
@RequiredArgsConstructor
public enum BeanFactoryProviders {

    UNSAFE(SingletonProvider.of(new UnsafeFactory())),
    CONSTRUCTOR(SingletonProvider.of(new ConstructorFactory())),
    FACTORY_METHOD(SingletonProvider.of(new FactoryMethodFactory())),
    ;

    public static BeanFactoryProviders DEFAULT;

    @Getter
    private final BeanFactoryProvider impl;
}
