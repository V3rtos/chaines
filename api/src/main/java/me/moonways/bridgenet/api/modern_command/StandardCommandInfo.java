package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.modern_command.entity.EntityType;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;
import org.jetbrains.annotations.NotNull;

public interface StandardCommandInfo {

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
     * Получить интервал вводы команды.
     */
    IntervalInfo getInterval();
    
    /**
     * Установить тип сущности, которая может выполнить команду.
     *
     * @param entityType - тип сущности.
     */
    void setEntityType(EntityType entityType);

    /**
     * Установить интервал команде.
     *
     * @param interval - параметры интервала.
     */
    void setInterval(IntervalInfo interval);

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
}
