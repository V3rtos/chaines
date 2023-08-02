package me.moonways.bridgenet.bootstrap.hook.console.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.*;
import me.moonways.bridgenet.api.command.option.OnlyConsoleSenderOption;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.rsi.endpoint.Endpoint;
import me.moonways.bridgenet.rsi.service.RemoteServiceRegistry;
import me.moonways.bridgenet.rsi.service.ServiceInfo;

import java.util.List;
import java.util.Map;

@Command("rsi")
@CommandOption(OnlyConsoleSenderOption.class)
public class RemoteServiceCommand {

    @Inject
    private RemoteServiceRegistry rsiRegistry;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        session.getSender().sendMessage("§eDefault sub-commands list:");
        session.printDefaultMessage("§7* §f{1} - §e/rsi {0}");
    }

    @ProducerExecutor("info")
    @ProducerDescription("Print an information block of actuality RMI protocol state")
    public void infoCommand(CommandSession session) {
        final EntityCommandSender entityCommandSender = session.getSender();

        printTotalServices(entityCommandSender);
        printTotalEndpoints(entityCommandSender);
    }

    private void printTotalServices(EntityCommandSender sender) {
        final Map<String, ServiceInfo> servicesInfos = rsiRegistry.getServicesInfos();

        sender.sendMessage("Total services:");
        servicesInfos.forEach((name, serviceInfo) -> {

            String formattedMessage = String.format("- %s: %s", name, serviceInfo);
            sender.sendMessage(formattedMessage);
        });
    }

    private void printTotalEndpoints(EntityCommandSender sender) {
        final List<Endpoint> endpointsList = rsiRegistry.getEndpointController().getEndpoints();

        sender.sendMessage("Total endpoints:");
        endpointsList.forEach((endpoint) -> {

            String formattedMessage = String.format("- %s: %s", endpoint.getServiceInfo().getName(), endpoint.getPath());
            sender.sendMessage(formattedMessage);
        });
    }
}
