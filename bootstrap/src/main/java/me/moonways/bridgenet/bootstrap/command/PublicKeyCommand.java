package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.rest4j.server.accesstoken.AccessToken;
import me.moonways.bridgenet.rest4j.server.accesstoken.AccessTokenSource;
import me.moonways.bridgenet.rest4j.server.accesstoken.Bridgenet4jAccessTokenService;

@Command("publickey")
public class PublicKeyCommand {

    @Inject
    private Bridgenet4jAccessTokenService accessTokenService;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        EntityCommandSender commandSender = session.getSender();

        if (commandSender instanceof ConsoleCommandSender) {
            commandSender.sendMessage(ChatColor.RED + "This command can use only players!");
            return;
        }

        AccessToken accessToken = accessTokenService.grantAccessToken(AccessTokenSource.CLIENT);
        commandSender.sendMessage("Ваш API-ключ для использования REST-API: " + accessToken.getToken());
    }
}
