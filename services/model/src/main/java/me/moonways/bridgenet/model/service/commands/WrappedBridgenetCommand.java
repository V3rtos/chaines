package me.moonways.bridgenet.model.service.commands;

import lombok.Builder;
import lombok.Getter;

import java.rmi.RemoteException;
import java.util.Optional;

@Builder
public class WrappedBridgenetCommand {

    @Getter
    private final BridgenetCommand baseCommand;
    private BridgenetCommand subCommand;

    public String getName() {
        try {
            return isNullSubElement() ? baseCommand.getConfiguration().getName() : subCommand.getConfiguration().getName();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<BridgenetCommand> getSubElement() {
        return Optional.ofNullable(subCommand);
    }

    private boolean isNullSubElement() {
        return subCommand == null;
    }
}
