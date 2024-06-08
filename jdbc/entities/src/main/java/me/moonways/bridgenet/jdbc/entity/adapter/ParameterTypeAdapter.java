package me.moonways.bridgenet.jdbc.entity.adapter;

import me.moonways.bridgenet.jdbc.entity.descriptor.EntityParametersDescriptor;

/**
 * Интерфейс, реализующий сериализацию одного
 * типа объекта в другой.
 */
public interface ParameterTypeAdapter {

    /**
     * Проверить, подходит ли обрабатываемый юнит
     * под процесс сериализации данного адаптера типов.
     *
     * @param unit - проверяемый юнит сущности.
     */
    boolean canSerialize(EntityParametersDescriptor.ParameterUnit unit);

    /**
     * Проверить, подходит ли обрабатываемый юнит
     * под процесс десериализации данного адаптера типов.
     *
     * @param unit - проверяемый юнит сущности.
     */
    boolean canDeserialize(EntityParametersDescriptor.ParameterUnit unit);

    /**
     * Выполнить процесс сериализации и вернуть новое
     * значение юнита, которое уже наследует необходимый тип
     * сериализованного объекта
     *
     * @param unit - обрабатываемый юнит сущности.
     */
    Object serialize(EntityParametersDescriptor.ParameterUnit unit);

    /**
     * Выполнить процесс десериализации и вернуть новое
     * значение юнита, которое уже наследует необходимый тип
     * десериализованного объекта
     *
     * @param unit - обрабатываемый юнит сущности.
     */
    Object deserialize(EntityParametersDescriptor.ParameterUnit unit);

    /**
     * Получить класс, в который сериализует данный адаптер.
     */
    Class<?> getOutputSerializationType();

    /**
     * Получить класс, в который десериализует данный адаптер.
     */
    Class<?> getOutputDeserializationType();
}
