package me.moonways.bridgenet.api.inject.bean.factory;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@RequiredArgsConstructor
public enum BeanFactoryProviders {

    UNSAFE(SingletonProvider.of(new UnsafeFactory())),
    CONSTRUCTOR(SingletonProvider.of(new ConstructorFactory())),
    ;

    public static BeanFactoryProviders DEFAULT;

    @Getter
    private final BeanFactoryProvider impl;
}
