package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.entity.CommandEntity;
import me.moonways.bridgenet.api.modern_command.label.LabelParser;
import org.jetbrains.annotations.NotNull;

public class CommandManagementService {

    @Inject
    private CommandRegistry registry;

    @Inject
    private CommandContainer container;

    @Inject
    private CommandExecutor executor;

    @Inject
    private LabelParser labelParser;

    public void register(@NotNull Object object) {
        CommandInfo commandInfo = registry.register(object);

        for (String alias : commandInfo.getAliases()) {
            container.add(alias, commandInfo);
        }
    }

    public void unregister(@NotNull String name) {
        container.remove(name);
    }

    public void unregisterAll() {
        container.removeAll();
    }

    public CommandInfo get(@NotNull String name) {
        return container.get(name.toLowerCase());
    }

    public boolean exists(@NotNull String name) {
        return get(name) != null;
    }

    public void execute(@NotNull CommandEntity entity, @NotNull String label) {
        String name = labelParser.getName(label);

        if (!exists(name)) {
            failExecute(entity);
            return;
        }

        String[] arguments = labelParser.getArguments(label);
        CommandInfo info = get(name);

        executor.execute(entity, info, arguments);
    }

    public void failExecute(@NotNull CommandEntity entity) {
        entity.sendMessage("Command not found :/");
    }
}
