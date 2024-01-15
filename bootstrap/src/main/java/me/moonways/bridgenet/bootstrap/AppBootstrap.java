package me.moonways.bridgenet.bootstrap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.event.EventManager;
import me.moonways.bridgenet.api.jaxb.XmlJaxbParser;
import me.moonways.bridgenet.api.scheduler.Scheduler;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookContainer;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookPriority;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

@Log4j2
public class AppBootstrap {

    @Getter
    private final BootstrapHookContainer hooksContainer = new BootstrapHookContainer();

    @Getter
    private DependencyInjection dependencyInjection;

    private void executeHooks(@NotNull BootstrapHookPriority priority) {
        Collection<BootstrapHook> hooksByPriority = hooksContainer.findOrderedHooks(priority);
        if (hooksByPriority == null)
            return;

        log.info("AppBootstrap.executeHooks() => begin: (priority={});", priority);
        hooksByPriority.forEach(bootstrapHook -> {

            String namespace = hooksContainer.findHookName(bootstrapHook.getClass());

            bootstrapHook.prepareExecution();
            bootstrapHook.execute(this, namespace);
        });

        log.info("AppBootstrap.executeHooks() => end;");
    }

    private void injectProject() {
        log.info("Running Dependency Injection search & bind processes");

        dependencyInjection = new DependencyInjection();

        injectApi(dependencyInjection);

        dependencyInjection.searchByProject();
        dependencyInjection.injectFields(hooksContainer);
    }

    private void injectApi(DependencyInjection injection) {
        final Object[] bindArr = new Object[]
                {
                        new XmlJaxbParser(),
                        new Scheduler(),
                        new EventManager(),
                };

        Stream.of(bindArr)
                .forEachOrdered(injection::bind);
    }

    public void start(String[] args) {
        log.info("Running Bridgenet bootstrap process with args = {}", Arrays.toString(args));

        injectProject();
        hooksContainer.bindHooks();

        executeHooks(BootstrapHookPriority.RUNNER);
        executeHooks(BootstrapHookPriority.POST_RUNNER);

        final Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        log.info("§4Shutting down Bridgenet services");

        executeHooks(BootstrapHookPriority.PRE_SHUTDOWN);
        executeHooks(BootstrapHookPriority.SHUTDOWN);

        Threads.shutdownForceAll();
        Runtime.getRuntime().halt(0);
    }
}
