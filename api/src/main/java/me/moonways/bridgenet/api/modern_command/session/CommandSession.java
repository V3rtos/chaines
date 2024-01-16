package me.moonways.bridgenet.api.modern_command.session;

import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;

public interface CommandSession {

    /**
     * Получить сущность выполняющую команду.
     */
    CommandEntity getEntity();

    /**
     * Получить требуемый тип сущности.
     *
     * @param entityClass - сущность, которую хотим получить.
     */
    <E extends CommandEntity> E from(Class<E> entityClass);

    /**
     * Заблокировать сессию.
     *
     * @param millis - время в миллисекундах.
     */
    void block(long millis);

    /**
     * Навсегда заблокировать сессию.
     */
    void block();
}
