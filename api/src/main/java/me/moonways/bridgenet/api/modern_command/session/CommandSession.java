package me.moonways.bridgenet.api.modern_command.session;

import me.moonways.bridgenet.api.modern_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.args.ArgumentWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

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
     * Получить имя команды.
     */
    String getCommandName();

    /**
     * Закрыть сессию.
     *
     * @param reason - причина.
     */
    void close(@NotNull String reason);

    /**
     * Получить требуемый тип сущности.
     *
     * @param entityClass - сущность, которую хотим получить.
     */
    <E extends EntityCommandSender> E from(Class<E> entityClass);

    /**
     * Заблокировать сессию.
     *
     * @param number - число.
     * @param time - время.
     */
    void block(long number, TimeUnit time);

    /**
     * Получить обёртку для работы с аргументами.
     */
    ArgumentWrapper getArgument();

    /**
     * Навсегда заблокировать сессию.
     */
    void block();
}
