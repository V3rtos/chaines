package me.moonways.bridgenet.api.modern_command.entity;

public class ConsoleEntity implements CommandEntity {

    @Override
    public void sendMessage(String message) {

    }

    @Override
    public void sendMessage(ColorMessage message) {

    }

    @Override
    public EntityType getType() {
        return EntityType.CONSOLE;
    }

    @Override
    public boolean valueOf(EntityType entityType) {
        return entityType.equals(EntityType.CONSOLE) || entityType.equals(EntityType.BOTH);
    }
}
