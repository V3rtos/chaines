package me.moonways.bridgenet.api.modern_command.data;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

@Getter
@Setter
public class Command extends AbstractCommandInfo {

    private List<Subcommand> subcommands;

    public Command(@NotNull Object parent, @NotNull Method handle, @NotNull String[] aliases) {
        super(parent, handle, aliases);
    }

    @Override
    public CommandType getCommandType() {
        return CommandType.COMMAND;
    }
}
