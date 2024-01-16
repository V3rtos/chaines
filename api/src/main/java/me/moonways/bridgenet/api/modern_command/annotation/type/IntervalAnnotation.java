package me.moonways.bridgenet.api.modern_command.annotation.type;

import me.moonways.bridgenet.api.modern_command.CommandInfo;
import me.moonways.bridgenet.api.modern_command.Interval;
import me.moonways.bridgenet.api.modern_command.annotation.CustomAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.RegistrationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.VerificationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;
import me.moonways.bridgenet.api.modern_command.interval.IntervalInfo;

@CustomAnnotation
public class IntervalAnnotation implements RegistrationCommandAnnotation<Interval, CommandInfo>, VerificationCommandAnnotation<Interval, CommandInfo> {

    @Override
    public void register(Interval interval, CommandInfo commandInfo) {
        commandInfo.setInterval(new IntervalInfo(interval.time(), interval.unit()));
    }

    @Override
    public boolean verify(Interval annotation, CommandInfo commandInfo, CommandEntity entity) {
        return false;
    }
}
