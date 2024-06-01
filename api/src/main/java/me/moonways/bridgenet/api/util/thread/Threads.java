package me.moonways.bridgenet.api.util.thread;

import lombok.RequiredArgsConstructor;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@UtilityClass
public class Threads {

    private final ThreadFactory THREAD_FACTORY = new WrappedThreadsFactory(Executors.defaultThreadFactory());

    private final List<ExecutorService> EXECUTOR_SERVICES = new ArrayList<>();
    private final List<Runnable> SHUTDOWN_HOOKS = new ArrayList<>();

    @RequiredArgsConstructor
    private static class WrappedThreadsFactory implements ThreadFactory {

        private final ThreadFactory handle;

        @Override
        public Thread newThread(@NotNull Runnable r) {
            Thread thread = handle.newThread(r);
            interceptThreadExceptions(thread);
            return thread;
        }

        private void interceptThreadExceptions(Thread thread) {
            thread.setUncaughtExceptionHandler(new WrappedUncaughtExceptionHandler());
        }
    }

    @Log4j2
    private static class WrappedUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {
        private static final String LOG_MESSAGE = ChatColor.RED + "Thread {} caught an exception that prevented its task from executing correctly: {}";

        @Override
        public void uncaughtException(Thread thread, Throwable exception) {
            log.error(LOG_MESSAGE, thread.getName(), exception.toString(), exception);
        }
    }

    public ExecutorService newSingleThreadExecutor() {
        ExecutorService executorService = Executors.newSingleThreadExecutor(THREAD_FACTORY);
        pull(executorService);
        return executorService;
    }

    public ExecutorService newCachedThreadPool() {
        ExecutorService executorService = Executors.newCachedThreadPool(THREAD_FACTORY);
        pull(executorService);
        return executorService;
    }

    public ExecutorService newWorkSteelingPool() {
        ExecutorService executorService = new ForkJoinPool
                (Runtime.getRuntime().availableProcessors(),
                        ForkJoinPool.defaultForkJoinWorkerThreadFactory,
                        new WrappedUncaughtExceptionHandler(), true);
        pull(executorService);
        return executorService;
    }

    public ScheduledExecutorService newSingleThreadScheduledExecutor() {
        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor(THREAD_FACTORY);
        pull(executorService);
        return executorService;
    }

    public ScheduledExecutorService newScheduledThreadPool(int corePoolSize) {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(corePoolSize, THREAD_FACTORY);
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
