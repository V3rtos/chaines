package me.moonways.bridgenet.api.modern_command.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.argument.wrapper.CommandArgumentWrapper;
import me.moonways.bridgenet.api.modern_command.argument.wrapper.CommandArgumentWrapperImpl;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class CommandSessionImpl implements CommandSession {

    private final EntityCommandSender entity;
    private final UUID uuid;

    private final CommandArgumentWrapper argumentWrapper;

    public CommandSessionImpl(EntityCommandSender entity, UUID uuid, String[] args) {
        this.entity = entity;
        this.uuid = uuid;

        this.argumentWrapper = new CommandArgumentWrapperImpl(args);
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public void close(@NotNull String reason) {
        entity.sendMessage(reason);
    }

    @Override
    public <E extends EntityCommandSender> E from(Class<E> entityClass) {
        return null;
    }

    @Override
    public void block(long millis) {
    }

    @Override
    public void block() {
    }
}
