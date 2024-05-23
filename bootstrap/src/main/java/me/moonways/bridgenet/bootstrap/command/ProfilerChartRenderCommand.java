package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.CommandParameter;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.option.CommandParameterOnlyConsoleUse;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.Async;
import me.moonways.bridgenet.profiler.BridgenetDataLogger;
import me.moonways.bridgenet.profiler.ProfilerType;

@Alias("profiler")
@Alias("profiling")
@Alias("metric")
@Alias("metrics")
@Command("profilers")
@CommandParameter(CommandParameterOnlyConsoleUse.class)
@EnableDecorators
public class ProfilerChartRenderCommand {

    @Inject
    private BridgenetDataLogger bridgenetDataLogger;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        for (ProfilerType profilerType : ProfilerType.values()) {
            receiveRenderedImage(profilerType, session.getSender());
        }
    }

    @Async
    public void receiveRenderedImage(ProfilerType profilerType, EntityCommandSender sender) {
        String renderedImageUrl = bridgenetDataLogger.renderProfilerChart(profilerType);
        sender.sendMessage("Illustration of the §7\"%s\" §rmetric is prepared from the link: %s", profilerType.getDisplayName(), renderedImageUrl);
    }
}
