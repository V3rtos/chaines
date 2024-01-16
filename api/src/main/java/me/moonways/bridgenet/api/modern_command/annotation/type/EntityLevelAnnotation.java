package me.moonways.bridgenet.api.modern_command.annotation.type;

import me.moonways.bridgenet.api.modern_command.CommandInfo;
import me.moonways.bridgenet.api.modern_command.EntityLevel;
import me.moonways.bridgenet.api.modern_command.annotation.CustomAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.RegistrationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.VerificationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;

@CustomAnnotation
public class EntityLevelAnnotation implements RegistrationCommandAnnotation<EntityLevel, CommandInfo>, VerificationCommandAnnotation<EntityLevel, CommandInfo> {

    @Override
    public void register(EntityLevel entityLevel, CommandInfo commandInfo) {
        commandInfo.setEntityType(entityLevel.value());
    }

    @Override
    public boolean verify(EntityLevel annotation, CommandInfo commandInfo, CommandEntity entity) {
        return commandInfo.getEntityType().equals(entity.getType());
    }
}
