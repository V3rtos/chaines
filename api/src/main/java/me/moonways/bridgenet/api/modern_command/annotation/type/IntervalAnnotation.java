package me.moonways.bridgenet.api.modern_command.annotation.type;

import me.moonways.bridgenet.api.modern_command.CommandInfo;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.UsageCooldown;
import me.moonways.bridgenet.api.modern_command.annotation.CustomAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.RegistrationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.VerificationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;

@CustomAnnotation
public class IntervalAnnotation implements
        RegistrationCommandAnnotation<UsageCooldown, CommandInfo>,
        VerificationCommandAnnotation<UsageCooldown, CommandInfo> {

    @Override
    public void register(UsageCooldown interval, CommandInfo commandInfo) {
        commandInfo.setInterval(new IntervalInfo(interval.time(), interval.unit()));
    }

    @Override
    public boolean verify(UsageCooldown annotation, CommandInfo commandInfo, CommandEntity entity) {
        return false;
    }
}
