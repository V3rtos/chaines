package me.moonways.bridgenet.api.modern_command.cooldown.info;

import java.util.concurrent.TimeUnit;

public interface CooldownInfo {

    long getDuration();

    TimeUnit getTime();
}
