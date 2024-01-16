package me.moonways.bridgenet.api.util.thread;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@UtilityClass
public class Threads {

    private final List<ExecutorService> EXECUTOR_SERVICES = new ArrayList<>();
    private final List<Runnable> SHUTDOWN_HOOKS = new ArrayList<>();

    public ExecutorService newCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        pull(executorService);
        return executorService;
    }

    public ScheduledExecutorService newSingleThreadScheduledExecutor() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        pull(executorService);
        return executorService;
    }

    public ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(corePoolSize);
        pull(executorService);
        return executorService;
    }

    public void pull(ExecutorService executorService) {
        EXECUTOR_SERVICES.add(executorService);
    }

    public void hookShutdown(Runnable runnable) {
        SHUTDOWN_HOOKS.add(runnable);
    }

    public void shutdownForceAll() {
        for (Runnable shutdownHook : SHUTDOWN_HOOKS) {
            shutdownHook.run();
        }
        for (ExecutorService executorService : EXECUTOR_SERVICES) {
            executorService.shutdownNow();
        }
    }
}
