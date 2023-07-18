package me.moonways.bridgenet.bootstrap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookContainer;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookPriority;
import me.moonways.bridgenet.injection.DependencyInjection;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.stream.Collectors;

@Log4j2
public class AppBootstrap {

    @Getter
    private final BootstrapHookContainer hooksContainer = new BootstrapHookContainer();

    private final DependencyInjection dependencyInjection = new DependencyInjection();

    private void forceShutdown() {
        System.exit(1);
    }

    private String joinHooksToNamesLine(Collection<BootstrapHook> registeredHooks) {
        return registeredHooks.stream()
                .map(instance -> hooksContainer.findHookName(instance.getClass()))
                .collect(Collectors.joining(", "));
    }

    private Collection<BootstrapHook> findHooksByPriority(@NotNull BootstrapHookPriority priority) {
        Collection<BootstrapHook> registeredHooks = hooksContainer.getRegisteredHooks(priority);

        if (registeredHooks.size() > 1) {
            if (registeredHooks.stream()
                    .filter(hook -> hooksContainer.findHookPriorityID(hook.getClass()) >= 0)
                    .count() != registeredHooks.size()) {

                log.error("§4Registered hooks ({}) is not marked by priority ID", joinHooksToNamesLine(registeredHooks));
                forceShutdown();

                return null;
            }

            Comparator<BootstrapHook> comparator = Comparator.comparingInt(
                    hook -> hooksContainer.findHookPriorityID(hook.getClass()));

            registeredHooks = registeredHooks.stream().sorted(comparator).collect(Collectors.toList());
        }

        log.info("Found §6{} §rregistered hooks: [{}]", registeredHooks.size(), joinHooksToNamesLine(registeredHooks));
        return registeredHooks;
    }

    private void executeHooks(@NotNull BootstrapHookPriority priority) {
        Collection<BootstrapHook> hooksByPriority = findHooksByPriority(priority);
        if (hooksByPriority == null)
            return;

        log.info("AppBootstrap.executeHooks() => begin: (priority={});", priority);
        hooksByPriority.forEach(bootstrapHook -> {

            String namespace = hooksContainer.findHookName(bootstrapHook.getClass());
            dependencyInjection.injectFields(bootstrapHook);

            bootstrapHook.setProperties();
            bootstrapHook.execute(this, namespace);
        });

        log.info("AppBootstrap.executeHooks() => end;");
    }

    private void injectProject() {
        log.info("Running Dependency Injection search & bind processes");

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

        Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        log.info("Shutting down Bridgenet services & execute hooks");

        executeHooks(BootstrapHookPriority.PRE_SHUTDOWN);
        executeHooks(BootstrapHookPriority.SHUTDOWN);

        System.exit(0);
    }
}
