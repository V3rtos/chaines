package me.moonways.bridgenet.test.api.command.modern;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.modern_x2_command.*;
import me.moonways.bridgenet.api.modern_x2_command.api.regex.CommandRegex;
import me.moonways.bridgenet.api.modern_x2_command.api.regex.CommandRegexRegistry;
import me.moonways.bridgenet.api.modern_x2_command.objects.CommandExecutionContext;
import me.moonways.bridgenet.api.modern_x2_command.objects.entity.EntityCommandSender;
import me.moonways.bridgenet.api.modern_x2_command.objects.label.CommandLabelContext;
import me.moonways.bridgenet.api.modern_x2_command.CommandRegexId;
import me.moonways.bridgenet.api.modern_x2_command.process.execute.CommandExecuteResult;
import me.moonways.bridgenet.api.util.minecraft.ChatColor;
import me.moonways.bridgenet.model.players.PlayersServiceModel;
import me.moonways.bridgenet.test.engine.BridgenetJUnitTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.UUID;

@RunWith(BridgenetJUnitTestRunner.class)
public class TestCommand {

    @Inject
    private CommandRegexRegistry regexRegistry;

    @Inject
    private PlayersServiceModel playersServiceModel;

    @PostConstruct
    public void injectRegex() {
        regexRegistry.add("user_name", new CommandRegex("[a-zA-Z0-9_]{3,16}", "Тест регекса ника"));
    }

    @Test
    @GeneralCommand("test")
    public CommandExecuteResult general(CommandExecutionContext commandExecutionContext) {
        EntityCommandSender entityCommandSender = commandExecutionContext.getSender();
        return CommandExecuteResult.ok(entityCommandSender, entityCommandSender_ ->
                entityCommandSender_.sendMessage("Successful dispatch test command"));
    }

    @Test
    @SubCommand({"info", "player", "get"})
    @ComandArgHelper(
            value = {@CommandArg(position = 1, regexId = @CommandRegexId(value = "user_name")), @CommandArg(position = 2)}, //info (first arg) | <nick_name> (second arg)
            usage = "/test info <user_name>",
            description = "поиск игрока в базе данных")
    public CommandExecuteResult info(CommandExecutionContext commandExecutionContext) {
        EntityCommandSender entityCommandSender = commandExecutionContext.getSender();
        CommandLabelContext labelContext = commandExecutionContext.getLabel();
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
