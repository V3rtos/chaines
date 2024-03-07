package me.moonways.bridgenet.api.command.api.process.verification.impl.cooldown.info;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

@Getter
public class ExpirationCooldownInfoImpl extends CooldownInfoImpl implements CooldownInfo {

    private final String entityName;

    public ExpirationCooldownInfoImpl(String entityName, long duration, TimeUnit time) {
        super(duration, time);

        this.entityName = entityName;
    }
}
