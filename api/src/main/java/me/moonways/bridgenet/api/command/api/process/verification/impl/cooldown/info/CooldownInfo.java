package me.moonways.bridgenet.api.command.api.process.verification.impl.cooldown.info;

import java.util.concurrent.TimeUnit;

public interface CooldownInfo {

    long getDuration();

    TimeUnit getTime();
}
