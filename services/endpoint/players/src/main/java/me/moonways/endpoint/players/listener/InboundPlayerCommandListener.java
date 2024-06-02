package me.moonways.endpoint.players.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.command.CommandExecutor;
import me.moonways.bridgenet.api.command.exception.CommandExecutionException;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.model.message.Disconnect;
import me.moonways.bridgenet.model.message.Handshake;
import me.moonways.bridgenet.model.message.SendCommand;
import me.moonways.bridgenet.model.service.bus.HandshakePropertiesConst;
import me.moonways.bridgenet.model.service.players.Player;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;
import me.moonways.endpoint.players.PlayerStoreStub;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.rmi.RemoteException;
import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

@Log4j2
@InboundMessageListener
@RequiredArgsConstructor
public final class InboundPlayerCommandListener {
    private final PlayerStoreStub playerStoreStub;

    @Inject
    private CommandExecutor commandExecutor;

    @SubscribeMessage
    public void handle(InboundMessageContext<SendCommand> context) throws RemoteException {
        SendCommand message = context.getMessage();
        Optional<Player> playerOptional = playerStoreStub.get(message.getPlayerId());

        if (playerOptional.isPresent()) {
            Player player = playerOptional.get();

            try {
                commandExecutor.execute(new LegacyWrappedPlayerSender(player), message.getLabel());
                context.callback(new SendCommand.Success());

            } catch (CommandExecutionException e) {
                context.callback(new SendCommand.Failure());
            }
        }
    }

    // todo - убрать после влития новой системы команд.
    @RequiredArgsConstructor
    private static class LegacyWrappedPlayerSender implements EntityCommandSender {
        private final Player player;

        @Override
        public void sendMessage(@Nullable String message) {
            try {
                player.sendMessage(message);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void sendMessage(@NotNull String message, @Nullable Object... replacements) {
            try {
                player.sendMessage(String.format(message, replacements));
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean hasPermission(@NotNull String permission) {
            try {
                return player.hasPermission(permission);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
