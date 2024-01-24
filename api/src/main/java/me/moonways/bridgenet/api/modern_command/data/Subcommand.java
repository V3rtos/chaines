package me.moonways.bridgenet.api.modern_command.data;

import lombok.Getter;
import lombok.Setter;

import java.lang.reflect.Method;

@Getter
@Setter
public class Subcommand extends AbstractCommandInfo {

    private String usage;

    public Subcommand(Object parent, Method handle, String[] aliases) {
        super(parent, handle, aliases);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.SUBCOMMAND;
    }
}
