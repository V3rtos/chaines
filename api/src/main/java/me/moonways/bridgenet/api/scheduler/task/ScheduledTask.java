package me.moonways.bridgenet.api.scheduler.task;

import me.moonways.bridgenet.api.scheduler.ScheduledTime;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Данный макет описывает базовый функционал
 * запланированных задач.
 */
public interface ScheduledTask {

    /**
     * Получение идентификатора, по которому
     * кешируется данная задача в системе.
     */
    long getId();

    /**
     * Единоразовая задержка при первом или единственном
     * выполнении запланированной задачи.
     */
    @NotNull
    ScheduledTime getDelay();

    /**
     * Периодичная задержка в выполнении, нужна
     * для реализации бесконечной задачи с определенной
     * задержкой между цикличными повторениями выполнения
     * актуальной задачи.
     * </p>
     * Если цикличное выполнение не требуется или задача
     * не подразумевает такую логику, то данный метод вернет null.
     */
    @Nullable
    ScheduledTime getPeriod();

    /**
     * Выполняется ли данная задача в отдельном
     * асинхронном потоке.
     */
    boolean isAsynchronous();

    /**
     * Была ли ранее отменена уже данная задача.
     */
    boolean isCancelled();

    /**
     * Прекратить выполнение задачи.
     */
    void shutdown();

    /**
     * Насильно прекратить выполнение задачи
     * без ожидания завершения текущего процесса (если
     * время уже подошло к ее исполнению)
     */
    void forceShutdown();
}

