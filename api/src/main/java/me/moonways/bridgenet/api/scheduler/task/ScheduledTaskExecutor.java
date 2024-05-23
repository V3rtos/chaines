package me.moonways.bridgenet.api.scheduler.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.scheduler.ScheduleException;
import me.moonways.bridgenet.api.scheduler.ScheduledQueue;
import me.moonways.bridgenet.api.scheduler.ScheduledTime;

import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Log4j2
@RequiredArgsConstructor
public class ScheduledTaskExecutor {

    private final ScheduledExecutorService executorService;

    private final ScheduledQueue scheduledQueue = new ScheduledQueue();

    private void validateNull(ScheduledTask scheduledTask) {
        if (scheduledTask == null) {
            throw new ScheduleException("scheduled task is null");
        }
    }

    private void validateNull(TaskFuture taskFuture) {
        if (taskFuture == null) {
            throw new ScheduleException("task future is null");
        }
    }

    private void pollFuture(TaskFuture taskFuture) {
        validateNull(taskFuture);

        ScheduledTask scheduledTask = taskFuture.getScheduledTask();

        if (scheduledTask.isCancelled() || scheduledTask.getPeriod() == null)
            scheduledQueue.poll(scheduledTask.getId());
    }

    private TaskFuture createFuture(ScheduledTask scheduledTask) {
        validateNull(scheduledTask);
        return new TaskFuture(scheduledTask, new TaskProcessFollower(scheduledTask));
    }

    private void executeSynchronized(TaskFuture taskFuture) {
        executorService.execute(new SyncTaskRunnable(taskFuture));
    }

    private Future<?> execute(TaskFuture taskFuture, ScheduledTask scheduledTask) {
        validateNull(scheduledTask);

        ExecutorTaskRunnable executorTaskRunnable = new ExecutorTaskRunnable(taskFuture);
        Future<?> future;

        boolean isInfinity = scheduledTask.getPeriod() != null;
        if (isInfinity)
            future = executorService.scheduleAtFixedRate(executorTaskRunnable, scheduledTask.getDelay().toNanos(),
                    scheduledTask.getPeriod().toNanos(), TimeUnit.NANOSECONDS);
        else
            future = executorService.schedule(executorTaskRunnable, scheduledTask.getDelay().toNanos(), TimeUnit.NANOSECONDS);

        log.info("Running execution {}", scheduledTask);
        return future;
    }

    private TaskFuture schedule0(boolean synchronize, ScheduledTaskFactory factory) {
        long taskId = scheduledQueue.nextId();
        ScheduledTask scheduledTask = factory.createTaskInstance(taskId);

        scheduledQueue.push(scheduledTask);

        TaskFuture taskFuture = createFuture(scheduledTask);
        if (synchronize)
            executeSynchronized(taskFuture);

        return taskFuture;
    }

    private TaskFuture schedule(ScheduledTaskFactory factory) {
        return schedule0(true, factory);
    }

    private TaskFuture scheduleExecutor(ScheduledTaskFactory factory) {
        TaskFuture taskFuture = schedule0(false, factory);
        Future<?> executionFuture = execute(taskFuture, taskFuture.getScheduledTask());

        ((AbstractDelayedTask) taskFuture.getScheduledTask()).setFuture(executionFuture);

        return taskFuture;
    }

    public synchronized TaskFuture scheduleSynchronized(ScheduledTime delay) {
        return schedule((id) -> new LateDelayedTask(id, delay));
    }

    public synchronized TaskFuture scheduleSynchronized(ScheduledTime delay, ScheduledTime period) {
        return schedule((id) -> new InfinityDelayedTask(id, delay, period));
    }

    public synchronized TaskFuture schedule(ScheduledTime delay) {
        return scheduleExecutor((id) -> new LateDelayedTask(id, delay));
    }

    public synchronized TaskFuture schedule(ScheduledTime delay, ScheduledTime period) {
        return scheduleExecutor((id) -> new InfinityDelayedTask(id, delay, period));
    }

    @RequiredArgsConstructor
    private class ExecutorTaskRunnable implements Runnable {

        private final TaskFuture taskFuture;

        @Override
        public void run() {
            if (scheduledQueue.peek(taskFuture.getScheduledTask().getId()) == null)
                return;

            taskFuture.post();
            pollFuture(taskFuture);
        }
    }

    @RequiredArgsConstructor
    private class SyncTaskRunnable implements Runnable {

        private final TaskFuture taskFuture;

        @Override
        public void run() {
            if (scheduledQueue.peek(taskFuture.getScheduledTask().getId()) == null)
                return;

            ScheduledTask scheduledTask = taskFuture.getScheduledTask();
            synchronized (taskFuture) {
                ScheduledTime delay = scheduledTask.getDelay();
                try {
                    if (!delay.isZero())
                        taskFuture.wait(delay.toMillis());

                    taskFuture.post();
                    pollFuture(taskFuture);
                } catch (InterruptedException exception) {
                    throw new ScheduleException(exception, "Internal task process exception - {0}", scheduledTask.getId());
                }

                ScheduledTime period = scheduledTask.getPeriod();
                if (period != null) {
                    while (!scheduledTask.isCancelled()) {
                        try {
                            if (!period.isZero())
                                taskFuture.wait(period.toMillis());

                            taskFuture.post();
                            pollFuture(taskFuture);
                        } catch (InterruptedException exception) {
                            throw new ScheduleException(exception, "Internal task process exception - {0}", scheduledTask.getId());
                        }
                    }
                }
            }
        }
    }
}
