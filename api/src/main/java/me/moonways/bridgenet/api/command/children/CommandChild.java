package me.moonways.bridgenet.api.command.children;

import java.lang.reflect.Method;

/**
 * Внутренний объект команды, задействующий обработку
 * определенного процесса - подкоманда/матчер/др.
 */
public interface CommandChild {

    /**
     * Получить объект родительской команды, в котором
     * находится данный обработчик.
     */
    Object getParent();

    /**
     * Получить метод, вызывающий обработку текущего
     * компонента обработчика.
     */
    Method getMethod();
}
