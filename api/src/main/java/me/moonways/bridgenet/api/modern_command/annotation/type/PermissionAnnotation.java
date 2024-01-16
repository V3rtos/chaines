package me.moonways.bridgenet.api.modern_command.annotation.type;

import me.moonways.bridgenet.api.modern_command.CommandInfo;
import me.moonways.bridgenet.api.modern_command.modern_annotation.persistance.Permission;
import me.moonways.bridgenet.api.modern_command.annotation.CustomAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.RegistrationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.annotation.VerificationCommandAnnotation;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;

@CustomAnnotation
public class PermissionAnnotation implements RegistrationCommandAnnotation<Permission, CommandInfo>, VerificationCommandAnnotation<Permission, CommandInfo> {

    @Override
    public void register(Permission permission, CommandInfo commandInfo) {
        commandInfo.setPermission(permission.value());
    }

    @Override
    public boolean verify(Permission annotation, CommandInfo commandInfo, CommandEntity entity) {
        return entity.hasPermission(annotation.value());
    }
}
