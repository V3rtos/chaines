package me.moonways.bridgenet.test.api.command.modern;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.modern_x2_command.*;
import me.moonways.bridgenet.api.modern_x2_command.obj.ExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.obj.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.obj.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.obj.pattern.PatternFormat;
import me.moonways.bridgenet.api.modern_x2_command.process.result.CommandExecuteResult;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.model.players.PlayersServiceModel;

import java.util.UUID;

@InjectCommand
public class TestCommand {

    @Inject
    private PlayersServiceModel playersServiceModel;

    @GeneralCommand("test")
    public CommandExecuteResult general(ExecutionContext executionContext) {
        EntityCommandSender entityCommandSender = executionContext.getSender();
        return CommandExecuteResult.ok(entityCommandSender, entityCommandSender_ ->
                entityCommandSender_.sendMessage("Successful dispatch test command"));
    }

    @SubCommand({"info", "player", "get"})
    @ArgSyntaxes(@ArgSyntax(
            position = 1,
            pattern = @Pattern(
                    enumFormat    = PatternFormat.LOWER_CASE,
                    stringFormat  = "$placeholder$=user_name[a-zA-Z0-9_]{3,16}$msg$=Строка должна содержать имя пользователя")))
    public CommandExecuteResult info(ExecutionContext executionContext) {
        EntityCommandSender entityCommandSender = executionContext.getSender();
        CommandLabelContext labelContext = executionContext.getLabel();
        CommandLabelContext.Arguments arguments = labelContext.getArguments();

        return CommandExecuteResult.ok(entityCommandSender, entityCommandSender_ -> {
            UUID playerUuid = arguments.first(playersServiceModel::findPlayerId).orElse(null);
            String playerName = arguments.first().orElse(null);

            if (playerUuid == null) {
                entityCommandSender.sendMessage(ChatColor.RED + "Игрок '%s' не найден в базе данных системы", playerName);
            } else {
                entityCommandSender.sendMessage(ChatColor.YELLOW + "Игрок '%s' найден под идентификатором - %s", playerName, playerUuid);
            }
        });
    }
}
