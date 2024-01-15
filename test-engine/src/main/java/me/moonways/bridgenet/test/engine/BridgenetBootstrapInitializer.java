package me.moonways.bridgenet.test.engine;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.test.engine.exception.ExceptionFormatter;

@Log4j2
public final class BridgenetBootstrapInitializer {

    private static AppBootstrap bootstrap;
    private static final ExceptionFormatter EXCEPTION_FORMATTER = new ExceptionFormatter();

    public void init(Object testObject) {
        if (bootstrap == null) {

            log.info("Running initialization Bridgenet systems");
            log.info("**************** BRIDGENET TEST ENGINE START ****************");

            bootstrap = new AppBootstrap();
            bootstrap.start(new String[0]);
        }

        DependencyInjection dependencyInjection = bootstrap.getDependencyInjection();

        dependencyInjection.bind(testObject);
    }

    public void throwException(Throwable exception) {
        log.error(EXCEPTION_FORMATTER.formatToString(exception));
    }

    public void shutdown() {
        log.info("**************** BRIDGENET TEST ENGINE END ****************");
        bootstrap.shutdown();
    }
}
