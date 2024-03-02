package me.moonways.endpoint.bus.handler;

import me.moonways.bridgenet.api.command.CommandRegistry;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.GetCommands;
import me.moonways.bridgenet.mtp.message.InputMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.IncomingMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;

import java.util.Arrays;

@IncomingMessageListener
public class GetCommandsMessageHandler {

    @Inject
    private CommandRegistry registry;

    @SubscribeMessage
    public void handle(InputMessageContext<GetCommands> input) {
        GetCommands.Result commandsGetResult = new GetCommands.Result(
                Arrays.asList(registry.getRegisteredCommandsArray()));

        input.answer(commandsGetResult);
    }
}
