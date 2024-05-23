package me.moonways.bridgenet.bootstrap;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.concurrent.Executors;

public class AppStarter {

    private static final String THREAD_POOL_NAME = "bridgenet-bootstrap";

    public static void main(String[] args) {
        PreviousLogsCompressor.compressToGzip();
        Executors.newCachedThreadPool(new DefaultThreadFactory(THREAD_POOL_NAME))
                .submit(() -> bootstrap(args));
    }

    private static void bootstrap(String[] args) {
        new AppBootstrap().start(args);
    }
}
