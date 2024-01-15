package me.moonways.bridgenet.bootstrap.hook.type;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.autorun.ScheduledRunnersService;
import me.moonways.bridgenet.bootstrap.AppBootstrap;
import me.moonways.bridgenet.bootstrap.hook.ApplicationBootstrapHook;
import org.jetbrains.annotations.NotNull;

public class StartScheduledRunnersServiceHook extends ApplicationBootstrapHook {

    @Inject
    private ScheduledRunnersService scheduledRunnersService;

    @Override
    protected void process(@NotNull AppBootstrap bootstrap) throws Exception {
        scheduledRunnersService.start();
    }
}
