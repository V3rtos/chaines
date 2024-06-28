package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.CommandParameter;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.option.CommandParameterOnlyConsoleUse;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Alias("profiler")
@Alias("profiling")
@Alias("metric")
@Alias("metrics")
@Command("profilers")
@CommandParameter(CommandParameterOnlyConsoleUse.class)
public class ProfilerChartRenderCommand {
    private final ExecutorService threadExecutor = Executors.newWorkStealingPool();

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        for (ProfilerType profilerType : ProfilerType.values()) {
            receiveRenderedImage(profilerType, session.getSender());
        }
    }

    public void receiveRenderedImage(ProfilerType profilerType, EntityCommandSender sender) {
        threadExecutor.submit(() -> {

            String renderedImageUrl = bridgenetDataLogger.renderProfilerChart(profilerType);
            sender.sendMessage("Illustration of the §7\"%s\" §rmetric is prepared from the link: %s", profilerType.getDisplayName(), renderedImageUrl);
        });
    }
}
