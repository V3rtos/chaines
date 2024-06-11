package me.moonways.bridgenet.api.command.sender;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Данный интерфейс реализуется всемивозможными
 * в системе отправителями команд.
 */
public interface EntityCommandSender {

    String getName();

    /**
     * Отправить отправителю сообщение.
     *
     * @param message - сообщение.
     */
    void sendMessage(@Nullable String message);

    /**
     * Отправить отправителю форматированное сообщение.
     *
     * @param message      - сообщение.
     * @param replacements - параметры для вызова String#format()
     */
    void sendMessage(@NotNull String message, @Nullable Object... replacements);

    /**
     * Проверка на наличие конкретного права у отправителя.
     *
     * @param permission - право
     */
    boolean hasPermission(@NotNull String permission);
}
