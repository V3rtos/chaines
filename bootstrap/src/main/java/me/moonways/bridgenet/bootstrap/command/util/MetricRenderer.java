package me.moonways.bridgenet.bootstrap.command.util;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.Async;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;

@Autobind
@EnableDecorators
public class MetricRenderer {

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @Async
    public void sendRenderedMetricURL(ProfilerType profilerType, EntityCommandSender sender) {
        String illustrationURL = bridgenetDataLogger.requestIllustrationURL(profilerType);
        sender.sendMessage("Illustration of the §7\"%s\" §rmetric is prepared from the link: %s", profilerType.getDisplayName(), illustrationURL);
    }
}