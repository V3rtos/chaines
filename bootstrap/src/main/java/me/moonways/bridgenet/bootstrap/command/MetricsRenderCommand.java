package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandHelper;
import me.moonways.bridgenet.api.command.GeneralCommand;
import me.moonways.bridgenet.api.command.InjectCommand;
import me.moonways.bridgenet.api.command.api.uses.CommandExecutionContext;
import me.moonways.bridgenet.api.command.api.uses.entity.EntitySenderType;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.bootstrap.command.util.MetricRenderer;
import me.moonways.bridgenet.metrics.MetricType;

@InjectCommand
public class MetricsRenderCommand {

    @Inject
    private MetricRenderer renderer;

    @GeneralCommand({"metric", "metrics"})
    @CommandHelper(senderType = EntitySenderType.CONSOLE)
    public void defaultCommand(CommandExecutionContext executionContext) {
        for (MetricType metricType : MetricType.values()) {
            renderer.sendRenderedMetricURL(metricType, executionContext.getSender());
        }
    }
}
