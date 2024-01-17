package me.moonways.bridgenet.api.modern_command;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CommandInfo extends AbstractCommandInfo {

    private final Object parent;

    private List<SubcommandInfo> subcommands;

    public CommandInfo(Object parent, String[] aliases) {
        super(aliases);
        this.parent = parent;
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.COMMAND;
    }
}
