package me.moonways.bridgenet.mtp;

import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.KeepTime;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.NetworkMessageHandlerList;
import me.moonways.bridgenet.mtp.message.NetworkMessagesService;
import me.moonways.bridgenet.mtp.message.response.ResponsibleMessageService;
import org.jetbrains.annotations.NotNull;

@Autobind
@EnableDecorators
public class BridgenetNetworkController {

    @Inject
    private NetworkMessagesService networkMessagesService;
    @Inject
    private BeansService beansService;
    @Inject
    private NetworkMessageHandlerList handlerList;
    @Inject
    private ResponsibleMessageService responsibleMessageService;

    @KeepTime
    public void bindMessages() {
        networkMessagesService.bindMessages(false);
    }

    @KeepTime
    public void bindMessagesWithDirectionReverse() {
        networkMessagesService.bindMessages(true);
    }

    @KeepTime
    public void bindMessageListeners() {
        handlerList.bindHandlers();
    }

    @KeepTime
    public void register(Object handler) {
        handlerList.bind(handler);
    }

    public synchronized void pull(@NotNull InboundMessageContext<?> context) {
        beansService.inject(context);
        handlerList.handle(context);

        long callbackID = context.getCallbackID();

        Object message = context.getMessage();

        if (responsibleMessageService.isWaiting(callbackID, message.getClass())) {
            responsibleMessageService.complete(callbackID, message);
        }
    }

    public boolean isAnnotatedConnectException(Throwable exception) {
        return exception.toString().contains("io.netty.channel.AbstractChannel$AnnotatedConnectException");
    }
}
