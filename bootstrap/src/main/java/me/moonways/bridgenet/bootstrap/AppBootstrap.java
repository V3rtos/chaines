package me.moonways.bridgenet.bootstrap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookContainer;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookPriority;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;

@Log4j2
public class AppBootstrap {

    @Getter
    private final BootstrapHookContainer hooksContainer = new BootstrapHookContainer();

    private void executeHooks(@NotNull BootstrapHookPriority priority) {
        Collection<BootstrapHook> hooksByPriority = hooksContainer.findOrderedHooks(priority);
        if (hooksByPriority == null)
            return;

        log.info("AppBootstrap.executeHooks() => begin: (priority={});", priority);
        hooksByPriority.forEach(bootstrapHook -> {

            String namespace = hooksContainer.findHookName(bootstrapHook.getClass());

            bootstrapHook.setProperties();
            bootstrapHook.execute(this, namespace);
        });

        log.info("AppBootstrap.executeHooks() => end;");
    }

    private void injectProject() {
        log.info("Running Dependency Injection search & bind processes");

        final DependencyInjection dependencyInjection = new DependencyInjection();

        dependencyInjection.bind(this);

        dependencyInjection.findComponentsIntoBasePackage();
        dependencyInjection.injectFields(hooksContainer);
    }

    public void start(String[] args) {
        log.info("Running Bridgenet bootstrap process with args = {}", Arrays.toString(args));

        injectProject();
        hooksContainer.injectHooks();

        executeHooks(BootstrapHookPriority.RUNNER);
        executeHooks(BootstrapHookPriority.POST_RUNNER);

        final Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        log.info("Shutting down Bridgenet services & execute hooks");

        executeHooks(BootstrapHookPriority.PRE_SHUTDOWN);
        executeHooks(BootstrapHookPriority.SHUTDOWN);

        System.exit(0);
    }
}
