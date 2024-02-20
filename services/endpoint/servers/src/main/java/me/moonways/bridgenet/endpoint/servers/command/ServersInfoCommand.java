package me.moonways.bridgenet.endpoint.servers.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.*;
import me.moonways.bridgenet.api.command.option.CommandParameterOnlyConsoleUse;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.model.servers.EntityServer;
import me.moonways.bridgenet.model.servers.ServersServiceModel;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Alias("servers")
@Command("server")
@CommandParameter(CommandParameterOnlyConsoleUse.class)
public class ServersInfoCommand {

    @Inject
    private ServersServiceModel servers;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        EntityCommandSender sender = session.getSender();

        sender.sendMessage("Список доступных команд:");
        session.printDefaultMessage("§e/server {0} §7- {1}");
    }

    @Alias("get")
    @ProducerExecutor("info")
    @ProducerUsageDescription("info <server-name>")
    @ProducerDescription("Get a server information by name")
    public void info(CommandSession session) throws RemoteException {
        Optional<String> serverName = session.arguments().get(0);

        if (!serverName.isPresent()) {
            session.getSender().sendMessage(ChatColor.RED + "Требуется дополнительно указать наименование нужного сервера");
            return;
        }

        Optional<EntityServer> serverOptional = servers.getServer(serverName.get());
        if (!serverOptional.isPresent()) {
            session.getSender().sendMessage(ChatColor.RED + "Указанный сервер под именем '%s' не зарегистрирован", serverName.get());
            return;
        }

        EntityServer entityServer = serverOptional.get();

        sendServerInfo(entityServer, session.getSender());
    }

    @ProducerExecutor("list")
    @ProducerDescription("Get a total servers list")
    public void list(CommandSession session) throws RemoteException {
        List<EntityServer> serversList = new ArrayList<>();
        serversList.addAll(servers.getDefaultServers());
        serversList.addAll(servers.getFallbackServers());

        List<String> serversNamesList = new ArrayList<>();

        for (EntityServer server : serversList) {
            serversNamesList.add(server.getName());
        }

        session.getSender().sendMessage(String.join(", ", serversNamesList));
    }

    private void sendServerInfo(EntityServer entityServer, EntityCommandSender sender) throws RemoteException {
        StringBuilder result = new StringBuilder();

        result.append(entityServer.getServerInfo()).append("\n");
        result.append(entityServer.getTotalOnline());

        sender.sendMessage(result.toString());
    }
}
