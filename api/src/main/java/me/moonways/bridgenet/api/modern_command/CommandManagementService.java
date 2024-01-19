package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;
import org.jetbrains.annotations.NotNull;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Данных интерфейс описывает управление командами.
 */
public interface CommandManagementService {

    /**
     * Выполнить команду.
     *
     * @param entity - сущность выполняющая команду.
     * @param label - команда.
     */
    void execute(@NotNull EntityCommandSender entity, @NotNull String label);

    /**
     * Проверить на истечение таймаута у пользователя.
     *
     * @param userName - имя отправителя.
     * @param commandName - имя команды.
     */
    boolean isExpiredCooldown(@NotNull String userName, @NotNull String commandName);

    /**
     * Установить пользователю таймаут на команду.
     *
     * @param userName - имя пользователя, которому нужно установить таймаут.
     * @param commandName - имя команды.
     * @param number - число.
     * @param time - тип времени.
     */
    void setCooldown(@NotNull String userName, @NotNull String commandName, long number, TimeUnit time);

    /**
     * Снять таймаут у пользователя.
     *
     * @param userName - пользователь.
     * @param commandName - имя команды.
     */
    void removeCooldown(@NotNull String userName, @NotNull String commandName);

    /**
     * Получить список всех таймаутов у пользователей.
     *
     * @param commandName - имя команда.
     * @return - список всех, у кого таймаут на команду.
     */
    Set<IntervalInfo> getCooldowns(@NotNull String commandName);

    /**
     * Снять таймаут всех пользователям.
     */
    void removeCooldowns();

    /**
     * Зарегистрировать команду.
     *
     * @param object - Объект команды.
     */
    void register(@NotNull Object object);

    /**
     * Снять регистрацию с команды.
     *
     * @param name - имя команды.
     */
    void unregister(@NotNull String name);

    /**
     * Снять регистрацию со всех команд.
     */
    void unregisterAll();
}
