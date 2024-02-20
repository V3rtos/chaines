package me.moonways.bridgenet.api.inject.bean.factory;

public interface BeanFactoryProvider {

    /**
     * Получение фабрики бинов, благодаря которой
     * в дальнейшем можно разными способами
     * создавать экземпляр бина и инициализировать его
     * параметры.
     */
    BeanFactory get();
}
