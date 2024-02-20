package me.moonways.bridgenet.api.inject.bean.factory;

@FunctionalInterface
public interface BeanFactory {

    /**
     * Фабрика для создания экземпляров бинов.
     *
     * @param cls - класс бина.
     * @return - готовый инстанс и экземпляр по подобию бина.
     */
    <T> T create(Class<T> cls);
}
