package me.moonways.endpoint.games.handler;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.CreateGame;
import me.moonways.bridgenet.model.bus.message.DeleteGame;
import me.moonways.bridgenet.model.bus.message.UpdateGame;
import me.moonways.bridgenet.mtp.message.InputMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.MessageHandler;
import me.moonways.bridgenet.mtp.message.persistence.MessageTrigger;
import me.moonways.endpoint.games.GamesContainer;

import java.util.UUID;

@MessageHandler
public class GamesInputMessageListener {

    @Inject
    private GamesContainer container;

    @MessageTrigger
    public void handle(InputMessageContext<CreateGame> context) {
        CreateGame message = context.getMessage();
        // todo

        context.answer(new CreateGame.Result(UUID.randomUUID(), UUID.randomUUID()));
    }

    @MessageTrigger
    public void handle(DeleteGame message) {
        // todo
    }

    @MessageTrigger
    public void handle(UpdateGame message) {
        // todo
    }
}
