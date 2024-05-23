package me.moonways.bridgenet.model.commands;

import lombok.Builder;
import lombok.Getter;
import me.moonways.bridgenet.model.commands.label.CommandLabel;
import me.moonways.bridgenet.model.players.Player;
import me.moonways.bridgenet.model.util.audience.EntityAudience;

import java.rmi.RemoteException;
import java.util.Optional;

@Getter
@Builder
public class CommandExecutionContext {

    private final WrappedBridgenetCommand entityCommand;
    private final CommandLabel label;

    private final EntityAudience executor;

    public String getBaseCommandName() {
        try {
            return entityCommand.getBaseCommand().getConfiguration().getName();
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<String> getSubCommandName() {
        if (!entityCommand.getSubElement().isPresent()) {
            return Optional.empty();
        }

        try {
            return Optional.of(entityCommand.getSubElement()
                    .get()
                    .getConfiguration()
                    .getName());
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Player> T of(Class<? extends EntityAudience> cls) {
        return (T) cls.cast(executor);
    }
}
