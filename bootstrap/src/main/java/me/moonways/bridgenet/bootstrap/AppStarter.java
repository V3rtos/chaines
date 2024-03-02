package me.moonways.bridgenet.bootstrap;

public class AppStarter {

    public static void main(String[] args) {
        PreviousLogsCompressor.compressToGzip();

        final AppBootstrap bootstrap = new AppBootstrap();
        bootstrap.start(args);
    }
}
