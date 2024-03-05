package me.moonways.bridgenet.api.modern_x2_command.objects;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class CommandInfo {

    @Getter
    private final String uid;

    @Getter
    @Setter
    private String accessKey;

    @Setter
    private String[] helpDescription;

    public CommandInfo(String commandName, @Nullable String accessKey) {
        this.uid = UUID.fromString(commandName).toString();

        this.accessKey = accessKey;
    }
}
