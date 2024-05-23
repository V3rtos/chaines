package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.CommandParameter;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.option.CommandParameterOnlyConsoleUse;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.command.util.MetricRenderer;
import me.moonways.bridgenet.profiler.ProfilerType;

@Alias("profiler")
@Alias("profiling")
@Alias("metric")
@Alias("metrics")
@Command("profilers")
@CommandParameter(CommandParameterOnlyConsoleUse.class)
public class CommandRenderProfiling {

    @Inject
    private MetricRenderer renderer;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        for (ProfilerType profilerType : ProfilerType.values()) {
            renderer.sendRenderedMetricURL(profilerType, session.getSender());
        }
    }
}
