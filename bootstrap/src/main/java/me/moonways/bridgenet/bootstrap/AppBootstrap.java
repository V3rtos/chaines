package me.moonways.bridgenet.bootstrap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.assembly.OverridenProperty;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookContainer;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookPriority;
import me.moonways.bridgenet.bootstrap.system.LoggersManager;
import me.moonways.bridgenet.bootstrap.system.OverridenPropertiesManager;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

@Log4j2
public class AppBootstrap {

    @Getter
    private final BootstrapHookContainer hooksContainer = new BootstrapHookContainer();
    @Getter
    private final BeansService beansService = new BeansService();

    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    public synchronized boolean isRunning() {
        return isRunning.get();
    }

    public synchronized void setRunning(Boolean state) {
        isRunning.set(state);
    }

    public void processBootstrapHooks(@NotNull BootstrapHookPriority priority) {
        Collection<BootstrapHook> hooksByPriority = hooksContainer.findOrderedHooks(priority);
        if (hooksByPriority == null)
            return;

        log.debug("AppBootstrap.processBootstrapHooks() => begin: (priority={});", priority);

        hooksByPriority.forEach(hook -> {

            String namespace = hooksContainer.findHookName(hook.getClass());
            hook.apply(this, namespace);
        });

        log.debug("AppBootstrap.processBootstrapHooks() => end;");
    }

    public void startBeansActivity(boolean needsFull) {
        log.info("Starting beans service processing");

        beansService.bind(System.getProperties());
        beansService.bind(this);

        if (needsFull) {
            beansService.start();
            beansService.inject(hooksContainer);
        }

        log.info("Bootstrap was started System: §7'{}' §rversion §7{}§r.",
                OverridenProperty.SYSTEM_NAME.get(),
                OverridenProperty.SYSTEM_VERSION.get());
    }

    public void runSystems() {
        OverridenPropertiesManager.INSTANCE.run();
        LoggersManager.INSTANCE.run();
    }

    public void start(String[] args) {
        setRunning(true);
        log.info("Running with args = {}", Arrays.toString(args));

        runSystems();
        startBeansActivity(true);

        hooksContainer.bindHooks();

        processBootstrapHooks(BootstrapHookPriority.RUNNER);
        processBootstrapHooks(BootstrapHookPriority.POST_RUNNER);

        Runtime runtime = Runtime.getRuntime();
        runtime.addShutdownHook(new Thread(this::shutdownApp));
    }

    public void justShutdown() {
        setRunning(false);
        log.info("§4Shutting down Bridgenet services");

        processBootstrapHooks(BootstrapHookPriority.PRE_SHUTDOWN);
        processBootstrapHooks(BootstrapHookPriority.SHUTDOWN);

        Threads.shutdownForceAll();
    }

    public void shutdownApp() {
        justShutdown();
        Runtime.getRuntime().halt(0);
    }
}
