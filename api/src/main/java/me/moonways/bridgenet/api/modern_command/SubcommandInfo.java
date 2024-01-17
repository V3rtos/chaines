package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class SubcommandInfo extends AbstractCommandInfo {

    private final Method method;

    private String description;
    private String usageDescription;

    public SubcommandInfo(Method method, String[] aliases) {
        super(aliases);

        this.method = method;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.SUBCOMMAND;
    }
}
