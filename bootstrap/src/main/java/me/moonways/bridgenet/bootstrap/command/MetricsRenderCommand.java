package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.CommandParameter;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.option.CommandParameterOnlyConsoleUse;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.command.util.MetricRenderer;
import me.moonways.bridgenet.metrics.MetricType;

@Alias("metric")
@Command("metrics")
@CommandParameter(CommandParameterOnlyConsoleUse.class)
public class MetricsRenderCommand {

    @Inject
    private MetricRenderer renderer;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        for (MetricType metricType : MetricType.values()) {
            renderer.sendRenderedMetricURL(metricType, session.getSender());
        }
    }
}
