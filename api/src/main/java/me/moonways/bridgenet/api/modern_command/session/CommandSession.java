package me.moonways.bridgenet.api.modern_command.session;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;

import java.util.UUID;

public interface CommandSession {

    /**
     * Получить UUID сессии.
     */
    UUID getUuid();

    /**
     * Получить сущность выполняющую команду.
     */
    EntityCommandSender getEntity();

    /**
     * Получить требуемый тип сущности.
     *
     * @param entityClass - сущность, которую хотим получить.
     */
    <E extends EntityCommandSender> E from(Class<E> entityClass);

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
