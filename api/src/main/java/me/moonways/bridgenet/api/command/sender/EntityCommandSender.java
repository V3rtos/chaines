package me.moonways.bridgenet.api.command.sender;

import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

/**
 * Данный интерфейс реализуется всеми возможными
 * в системе отправителями команд.
 */
public interface EntityCommandSender {

    /**
     * Отправить отправителю сообщение.
     * @param message - сообщение.
     */
    void sendMessage(@Nullable String message);

    /**
     * Отправить отправителю форматированное сообщение.
     *
     * @param message - сообщение.
     * @param replacements - параметры для вызова String#format()
     */
    void sendMessage(@NotNull String message, @Nullable Object... replacements);

    /**
     * Проверка на наличие конкретного права у отправителя.
     * @param permission - право
     */
    boolean hasPermission(@NotNull String permission);

    /**
     * Получить тип сущности.
     */
    EntityType getType();

    /**
     * Проверить является ли сущность требуемой.
     *
     * @param entityType - тип сущности.
     * @return - true/false.
     */
    boolean instanceOf(EntityType entityType);

    /**
     * Получить UUID сущности.
     */
    UUID getUuid();

}
