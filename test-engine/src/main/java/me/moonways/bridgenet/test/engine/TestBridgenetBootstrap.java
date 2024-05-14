package me.moonways.bridgenet.test.engine;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;

import java.io.File;

@Log4j2
public final class TestBridgenetBootstrap {

    private static AppBootstrap bootstrap;
    private static final TestEngineExceptionFormatter EXCEPTION_FORMATTER = new TestEngineExceptionFormatter();

    public BeansService init(Object testObject) {
        if (bootstrap == null) {

            log.info("Running initialization Bridgenet systems");
            log.info("**************** BRIDGENET TEST ENGINE START ****************");

            bootstrap = new AppBootstrap();
            bootstrap.start(new String[0]);
        }

        BeansService injector = bootstrap.getBeansService();
        injector.bind(testObject);

        return injector;
    }

    public void throwException(Throwable exception) {
        log.error(EXCEPTION_FORMATTER.formatToString(exception));
    }

    public void shutdown() {
        log.info("**************** BRIDGENET TEST ENGINE END ****************");
        bootstrap.shutdown();
    }
}
