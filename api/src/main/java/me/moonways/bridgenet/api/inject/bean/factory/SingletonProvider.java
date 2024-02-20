package me.moonways.bridgenet.api.inject.bean.factory;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SingletonProvider implements BeanFactoryProvider {

    public static SingletonProvider of(BeanFactory impl) {
        return new SingletonProvider(impl);
    }

    private final BeanFactory beanFactory;

    @Override
    public BeanFactory get() {
        return beanFactory;
    }
}
