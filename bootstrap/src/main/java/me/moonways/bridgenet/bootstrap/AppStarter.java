package me.moonways.bridgenet.bootstrap;

import java.util.concurrent.Executors;

public class AppStarter {

    public static void main(String[] args) {
        PreviousLogsCompressor.compressToGzip();

        Executors.newCachedThreadPool().submit(() -> {

            final AppBootstrap bootstrap = new AppBootstrap();
            bootstrap.start(args);
        });
    }
}
