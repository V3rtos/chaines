package me.moonways.bridgenet.bootstrap.command;

import me.moonways.bridgenet.api.command.CommandSession;
import me.moonways.bridgenet.api.command.annotation.Alias;
import me.moonways.bridgenet.api.command.annotation.Command;
import me.moonways.bridgenet.api.command.annotation.MentorExecutor;
import me.moonways.bridgenet.api.command.sender.ConsoleCommandSender;
import me.moonways.bridgenet.api.command.sender.EntityCommandSender;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.minecraft.ChatColor;
import me.moonways.bridgenet.rest4j.server.accesstoken.AccessToken;
import me.moonways.bridgenet.rest4j.server.accesstoken.AccessTokenSource;
import me.moonways.bridgenet.rest4j.server.accesstoken.Bridgenet4jAccessTokenService;

@Alias("publicapi")
@Command("publickey")
public class PublicKeyCommand {

    @Inject
    private Bridgenet4jAccessTokenService accessTokenService;

    @MentorExecutor
    public void defaultCommand(CommandSession session) {
        EntityCommandSender commandSender = session.getSender();
        AccessTokenSource accessTokenSource = AccessTokenSource.CLIENT;

        if (commandSender instanceof ConsoleCommandSender) {
            accessTokenSource = AccessTokenSource.SYSTEM;
        }

        AccessToken accessToken = accessTokenService.grantAccessToken(accessTokenSource);
        commandSender.sendMessage(ChatColor.YELLOW + "Ваш сгенерированный API-ключ для использования REST-API: " + accessToken.getToken());
    }
}
