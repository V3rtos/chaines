package me.moonways.bridgenet.api.modern_command.data;

import me.moonways.bridgenet.api.modern_command.cooldown.info.CooldownInfo;
import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public interface CommandInfo {

    Object getParent();

    /**
     * Получить метод для вызова.
     */
    Method getHandle();

    /**
     * Получить список вариаций ввода команды.
     */
    String[] getAliases();

    /**
     * Получить право на выполнение команды.
     */
    String getPermission();

    /**
     * Получить описание команды.
     */
    String getDescription();

    /**
     * Получить тип сущности, которая может выполнить команду.
     */
    EntityType getEntityType();

    /**
     * Получить интервал ввода команды.
     */
    CooldownInfo getCooldown();
    
    /**
     * Установить тип сущности, которая может выполнить команду.
     *
     * @param entityType - тип сущности.
     */
    void setEntityType(EntityType entityType);

    /**
     * Установить кд команде.
     *
     * @param cooldown - параметры интервала.
     */
    void setCooldown(CooldownInfo cooldown);

    /**
     * Установить право.
     *
     * @param value - значение.
     */
    void setPermission(@NotNull String value);

    /**
     * Установить описание.
     *
     * @param value - значение.
     */
    void setDescription(@NotNull String value);

    /**
     * Получить тип команды.
     *
     * @return - COMMAND/SUBCOMMAND
     */
    CommandType getCommandType();

    /**
     * Добавить данные в проперти.
     *
     * @param key - ключ.
     * @param value - значение.
     */
    void addProperty(@NotNull Object key, @NotNull Object value);

    /**
     * Удалить проперти.
     *
     * @param key - ключ.
     */
    void removeProperty(@NotNull Object key);

    /**
     * Получить значение из проперти.
     *
     * @param key - ключ.
     */
    Object getPropertyValue(@NotNull Object key);

    /**
     * Проверить существует ли проперти.
     *
     * @param key - ключ.
     */
    boolean isExistsProperty(@NotNull Object key);
}
