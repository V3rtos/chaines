package me.moonways.bridgenet.bootstrap;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.api.util.thread.Threads;
import me.moonways.bridgenet.assembly.util.FilesZipCompressionUtils;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookContainer;
import me.moonways.bridgenet.bootstrap.hook.BootstrapHookPriority;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Collection<ApplicationBootstrapHook> hooksByPriority = hooksContainer.findOrderedHooks(priority);
        if (hooksByPriority == null)
            return;

        log.info("AppBootstrap.processBootstrapHooks() => begin: (priority={});", priority);

        hooksByPriority.forEach(hook -> {

            String namespace = hooksContainer.findHookName(hook.getClass());
            hook.apply(this, namespace);
        });

        log.info("AppBootstrap.processBootstrapHooks() => end;");
    }

    public void startBeansActivity(boolean canStartingFull) {
        log.info("Starting beans service processing");

        beansService.bind(System.getProperties());
        beansService.bind(this);

        if (canStartingFull) {
            beansService.start(BeansService.generateDefaultProperties());
            beansService.inject(hooksContainer);
        }
    }

    public void start(String[] args) {
        setRunning(true);
        log.info("Running Bridgenet bootstrap process with args = {}", Arrays.toString(args));

        startBeansActivity(true);
        hooksContainer.bindHooks();

        processBootstrapHooks(BootstrapHookPriority.RUNNER);
        processBootstrapHooks(BootstrapHookPriority.POST_RUNNER);

        final Runtime runtime = Runtime.getRuntime();

        runtime.addShutdownHook(new Thread(this::shutdown));
    }

    public void shutdown() {
        setRunning(false);
        log.info("§4Shutting down Bridgenet services");

        processBootstrapHooks(BootstrapHookPriority.PRE_SHUTDOWN);
        processBootstrapHooks(BootstrapHookPriority.SHUTDOWN);

        Threads.shutdownForceAll();

        Runtime.getRuntime().halt(0);
    }
}
