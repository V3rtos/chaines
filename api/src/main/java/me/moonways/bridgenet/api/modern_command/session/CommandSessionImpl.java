package me.moonways.bridgenet.api.modern_command.session;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.api.modern_command.cooldown.dao.CooldownDao;
import me.moonways.bridgenet.api.modern_command.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_command.args.ArgumentWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class CommandSessionImpl implements CommandSession {

    @Getter
    private final String commandName;
    @Getter
    private final EntityCommandSender entity;

    @Getter
    private final ArgumentWrapper argumentWrapper;

    private final CooldownDao cooldownDao;

    @Override
    public UUID getUuid() {
        return entity.getUuid();
    }

    @Override
    public void close(@NotNull String reason) {
        entity.sendMessage(reason);
    }

    @Override
    public <E extends EntityCommandSender> E from(Class<E> entityClass) {
        return entityClass.cast(entity);
    }

    @Override
    public void block(long number, TimeUnit time) {
        cooldownDao.set(commandName, entity.getName(), number, time);
    }

    @Override
    public ArgumentWrapper getArgument() {
        return argumentWrapper;
    }

    @Override
    public void block() {
        cooldownDao.set(commandName, entity.getName(), -1, TimeUnit.SECONDS);
    }
}
