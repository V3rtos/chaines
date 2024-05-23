package me.moonways.bridgenet.bootstrap.hook;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import org.jetbrains.annotations.NotNull;

@Log4j2
@RequiredArgsConstructor
public abstract class BootstrapHook {

    public final void apply(@NotNull AppBootstrap bootstrap,
                            @NotNull String namespace) {

        log.debug("Hook: §7{} <-> start;", namespace);
        try {
            process(bootstrap);
            log.debug("Hook: §7{} <-> end;", namespace);
        } catch (Exception exception) {
            log.error("§4Aborted bootstrap hook execution '{}' caused by {}", namespace, exception.toString(), exception);
        }
    }

    protected abstract void process(@NotNull AppBootstrap bootstrap)
            throws Exception;
}
