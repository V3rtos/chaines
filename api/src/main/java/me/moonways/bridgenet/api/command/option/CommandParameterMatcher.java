package me.moonways.bridgenet.api.command.option;

import me.moonways.bridgenet.api.command.CommandSession;
import org.jetbrains.annotations.NotNull;

/**
 * Реализации этого интерфейса отвечают за
 * предварительную верификацию команды по отношению
 * к сессии перед ее исполнением.
 */
public interface CommandParameterMatcher {

    /**
     * Условие, при котором отрабатывает обработка
     * данной верификации.
     * @param session - сессия команды, в которой вызвана верификация
     */
    boolean matches(@NotNull CommandSession session);

    /**
     * Что будет происходить, если условие верификации прошло корректно.
     * @param session - сессия команды
     */
    void process(@NotNull CommandSession session);
}
