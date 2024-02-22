package me.moonways.bridgenet.api.modern_command.execution;

import me.moonways.bridgenet.api.modern_command.args.CommandArgument;
import me.moonways.bridgenet.api.modern_x2_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.depend.cooldown.dao.CooldownDao;

/**
 * Данных интерфейс описывает управление командами.
 */
public interface CommandManagementService {

    /**
     * Выполнение команды.
     *
     * @param entity - сущность выполняющая команду.
     * @param label - команда.
     */
    void handle(EntityCommandSender entity, String label);

    /**
     * Выполнение команды.
     *
     * @param entity - сущность выполняющая команду.
     * @param commandArgument - аргументы команды.
     */
    void handle(EntityCommandSender entity, CommandArgument commandArgument);

    /**
     * Получение Cooldown data access object.
     */
    CooldownDao getCooldownDao();

    /**
     * Регистрация команды.
     *
     * @param object - Объект команды.
     */
    void register(Object object);

    /**
     * Удаление команды.
     *
     * @param name - имя команды.
     */
    void unregister(String name);

    /**
     * Удаление регистрации со всех команд.
     */
    void unregisterAll();
}
