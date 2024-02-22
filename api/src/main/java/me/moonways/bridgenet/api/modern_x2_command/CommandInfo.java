package me.moonways.bridgenet.api.modern_x2_command;

import lombok.Getter;

import java.util.UUID;

public class CommandInfo {

    @Getter
    private final String uid;

    @Getter
    private String accessKey;

    public CommandInfo(String commandName) {
        this.uid = UUID.fromString(commandName).toString();
    }
}
