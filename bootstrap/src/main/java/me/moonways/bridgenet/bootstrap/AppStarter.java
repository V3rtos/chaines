package me.moonways.bridgenet.bootstrap;

import io.netty.util.concurrent.DefaultThreadFactory;
import me.moonways.bridgenet.api.util.thread.Threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppStarter {

    private static final String THREAD_POOL_NAME = "bridgenet-bootstrap";

    public static void main(String[] args) {
        PreviousLogsCompressor.compressToGzip();

        ExecutorService executorService = Executors.newCachedThreadPool(new DefaultThreadFactory(THREAD_POOL_NAME));
        Threads.pull(executorService);

        executorService.submit(() -> bootstrap(args));
    }

    private static void bootstrap(String[] args) {
        new AppBootstrap().start(args);
    }
}
