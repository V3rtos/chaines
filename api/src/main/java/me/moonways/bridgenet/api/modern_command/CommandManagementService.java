package me.moonways.bridgenet.api.modern_command;

import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_command.annotation.CommandAnnotationService;
import me.moonways.bridgenet.api.modern_command.session.CommandSession;
import me.moonways.bridgenet.api.modern_command.session.CommandSessionImpl;
import me.moonways.bridgenet.api.modern_command.syntax.CommandSyntaxParser;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandManagementService {

    private static final String COMMAND_NOT_FOUND_MSG = "Bridgenet | %s";

    @Inject
    private CommandRegistry registry;

    @Inject
    private CommandContainer container;

    @Inject
    private CommandExecutor executor;

    @Inject
    private CommandSyntaxParser syntaxParser;

    @Inject
    private CommandAnnotationService annotationService;

    public void execute(@NotNull EntityCommandSender entity, @NotNull String label) {
        String[] args = syntaxParser.splitLabel(label);

        String commandName = syntaxParser.getName(args);
        if (!isExistsCommand(commandName)) {
            writeCommandNotExists(entity);
            return;
        }

        String[] parsedArgs = trimArgs(args); //Обрезаем ненужную часть массива -> ban[0] gitcoder[1] -> gitcoder[0]

        CommandSession session = createSession(entity, entity.getUuid(), parsedArgs);

        if (isParentCommand(parsedArgs)) {
            executeParentCommand(session, commandName);
            return;
        }

        String subcommandName = parsedArgs[0];
        executeSubCommand(session, subcommandName);
    }

    public void unregister(@NotNull String name) {
        container.remove(name);
    }

    public void unregisterAll() {
        container.removeAll();
    }

    public void register(@NotNull Object object) {
        CommandInfo commandInfo = registry.register(object);

        for (String alias : commandInfo.getAliases()) {
            container.add(alias, commandInfo);
        }
    }

    private void executeParentCommand(@NotNull CommandSession session, @NotNull String commandName) {
        CommandInfo commandInfo = castInfo(commandName, CommandInfo.class);

        if (!annotationService.processCommandAnnotations(session, commandInfo.getParent().getClass())) { //верификация доступа к команде перед вызовом
            return;
        }

        executor.executeParent(session, commandInfo);
    }

    private void executeSubCommand(@NotNull CommandSession session, @NotNull String subcommandName) {
        SubcommandInfo subcommandInfo = castInfo(subcommandName, SubcommandInfo.class);

        executor.executeSub(session, subcommandInfo);
    }

    private String[] trimArgs(@NotNull String[] standardArgs) {
        return standardArgs.length <= 1 ? new String[]{} : syntaxParser.getArguments(1, standardArgs);
    }

    private void writeCommandNotExists(@NotNull EntityCommandSender entity) {
        String formattedMessage = COMMAND_NOT_FOUND_MSG.replace("%s", "Команда не найдена");

        entity.sendMessage(formattedMessage);
    }

    private CommandSession createSession(EntityCommandSender entity, UUID entityUUID, String[] args) {
        return new CommandSessionImpl(entity, entityUUID, args);
    }

    private  <T extends StandardCommandInfo> T castInfo(@NotNull String name, Class<T> cls) {
        return cls.cast(container.get(name.toLowerCase()));
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean isExistsCommand(@NotNull String name) {
        return container.get(name) != null;
    }

    private boolean isParentCommand(@NotNull String[] args) {
        return args.length == 0 || !isExistsCommand(args[0]); //размер аргументов равен 0 или не существует подкоманды по 1 аргументу
    }
}
