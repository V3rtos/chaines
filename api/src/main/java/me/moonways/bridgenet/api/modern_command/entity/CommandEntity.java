package me.moonways.bridgenet.api.modern_command.entity;

public interface CommandEntity {

    /**
     * Отправить сообщение сущности.
     *
     * @param message - сообщение.
     */
    void sendMessage(String message);

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
    boolean valueOf(EntityType entityType);

    /**
     * Проверить есть ли право у сущности на выполнение команды.
     *
     * @param value - право.
     * @return - true/false
     */
    boolean hasPermission(String value);
}
