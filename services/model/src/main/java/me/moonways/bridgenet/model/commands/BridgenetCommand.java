package me.moonways.bridgenet.model.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.model.commands.arguments.ArgumentsContext;

@Getter
@ToString
@RequiredArgsConstructor
public abstract class BridgenetCommand implements Command {

    protected static CommandResult FAILED =
            CommandResult.builder()
                    .type(CommandResult.Type.FAILED)
                    .build();
    protected static CommandResult CONFIRMED =
            CommandResult.builder()
                    .type(CommandResult.Type.CONFIRMED)
                    .build();

    private final CommandDescription description;

    @Override
    public void prepare(ArgumentsContext argumentsContext) {
        // override me.

        for (int i = 0; i < argumentsContext.count(); i++) {
            argumentsContext.setName(i, Integer.toString(i));
        }
    }

    @Override
    public CommandResult run(CommandSessionContext context) {
        // override me.
        return CommandResult.builder()
                .type(CommandResult.Type.FAILED)
                .failThrowableSuppler(() -> new UnsupportedOperationException("no execution logic"))
                .build();
    }
}
