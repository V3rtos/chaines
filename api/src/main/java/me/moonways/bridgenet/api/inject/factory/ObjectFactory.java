package me.moonways.bridgenet.api.inject.factory;

/**
 * Фабрика java-объектов по их классам.
 */
@FunctionalInterface
public interface ObjectFactory {

    /**
     * Создать экземпляр по подобию класса.
     *
     * @param cls - класс, из которого необходимо создать объект.
     * @return - готовый экземпляр.
     */
    <T> T create(Class<T> cls);
}
