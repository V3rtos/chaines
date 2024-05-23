package me.moonways.bridgenet.model.commands;

import lombok.Builder;
import lombok.Getter;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Builder
public class BridgenetCommand {

    private final UUID id;
    private final CommandConfiguration configuration;

    private final List<BridgenetCommand> subCommands;

    public BridgenetCommand getSubCommand(String name) {
        return subCommands.stream()
                .filter(element -> {
                    try {
                        return element.getConfiguration().getName().equalsIgnoreCase(name);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList())
                .stream()
                .findFirst().orElse(null);
    }

    public boolean hasSubCommand(String name) {
        return (long) (int) subCommands.stream()
                .filter(element -> {
                    try {
                        return element.getConfiguration().getName().equalsIgnoreCase(name);
                    } catch (RemoteException e) {
                        throw new RuntimeException(e);
                    }
                }).count() > 0;
    }

    public boolean isEmptySubCommand() {
        return subCommands.isEmpty();
    }
}
