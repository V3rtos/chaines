package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationService;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.session.CommandSessionImpl;
import me.moonways.bridgenet.api.modern_command.syntax.SyntaxParser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandManagementService {

    @Inject
    private CommandRegistry registry;

    @Inject
    private CommandContainer container;

    @Inject
    private CommandExecutor executor;

    @Inject
    private SyntaxParser syntaxParser;

    @Inject
    private CommandAnnotationService annotationService;

    public void execute(@NotNull EntityCommandSender entity, @NotNull String label) {
        String[] args = syntaxParser.splitCommandLabelIntoArray(label);

        String commandName = syntaxParser.getCommandName(args);
        if (!isExists(commandName)) {
            failExecute(entity);
            return;
        }

        CommandSession session = createSession(entity, entity.getUuid());

        String[] parsedArgs = args.length <= 1 ? new String[]{} : syntaxParser.getCommandArguments(1, args);

        CommandInfo commandInfo = getCasted(commandName, CommandInfo.class);
        if (parsedArgs.length == 0 || !isExists(parsedArgs[0])) { //args size = 0 no subcommand | no exists subcommand by args[1]
            if (!annotationService.processCommandAnnotations(session, commandInfo.getParent().getClass())) { //верификация доступа к команде перед вызовом
                return;
            }

            executor.executeParent(entity, commandInfo, parsedArgs);
            return;
        }

        String subcommandName = parsedArgs[0];
        SubcommandInfo subcommandInfo = getCasted(subcommandName, SubcommandInfo.class);

        executor.executeSub(entity, subcommandInfo, parsedArgs);
    }

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

    private void failExecute(@NotNull EntityCommandSender entity) {
        entity.sendMessage("Command not found :/");
    }

    private CommandSession createSession(EntityCommandSender entity, UUID entityUUID) {
        return new CommandSessionImpl(entity, entityUUID);
    }

    private  <T extends StandardCommandInfo> T getCasted(@NotNull String name, Class<T> cls) {
        return cls.cast(container.get(name.toLowerCase()));
    }

    private boolean isExists(@NotNull String name) {
        return container.get(name) != null;
    }
}
