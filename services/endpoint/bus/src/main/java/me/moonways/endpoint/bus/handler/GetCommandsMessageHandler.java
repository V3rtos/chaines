package me.moonways.endpoint.bus.handler;

import me.moonways.bridgenet.api.command.CommandRegistry;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.model.bus.message.GetCommands;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.persistence.InboundMessageListener;
import me.moonways.bridgenet.mtp.message.persistence.SubscribeMessage;

import java.util.Arrays;

@InboundMessageListener
public class GetCommandsMessageHandler {

    @Inject
    private CommandRegistry registry;

    @SubscribeMessage
    public void handle(InboundMessageContext<GetCommands> context) {
        GetCommands.Result commandsGetResult = new GetCommands.Result(
                Arrays.asList(registry.getRegisteredCommandsArray()));

        context.writeCallback(commandsGetResult);
    }
}
