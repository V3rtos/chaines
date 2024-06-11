package me.moonways.bridgenet.mtp;

import lombok.Getter;
import lombok.Synchronized;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.decorator.persistence.Async;
import me.moonways.bridgenet.api.inject.decorator.persistence.KeepTime;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorResult;
import me.moonways.bridgenet.api.inject.processor.persistence.GetTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.persistence.WaitTypeAnnotationProcessor;
import me.moonways.bridgenet.mtp.message.InboundMessageContext;
import me.moonways.bridgenet.mtp.message.NetworkMessageHandlerList;
import me.moonways.bridgenet.mtp.message.NetworkMessagesService;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import me.moonways.bridgenet.mtp.message.response.ResponsibleMessageService;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

@Getter
@Autobind
@EnableDecorators
@WaitTypeAnnotationProcessor({ClientMessage.class, ServerMessage.class})
public class BridgenetNetworkController implements Serializable {

    private static final long serialVersionUID = 2199366969666560453L;

    private NetworkMessagesService networkMessagesService;

    @Inject
    private BeansService beansService;
    @Inject
    private NetworkMessageHandlerList handlerList;
    @Inject
    private ResponsibleMessageService responsibleMessageService;

    @GetTypeAnnotationProcessor
    private TypeAnnotationProcessorResult<Object> messagesResult;

    @PostConstruct
    private void init() {
        List<Object> messagesList = messagesResult.toList();
        messagesList.sort(Comparator.comparing(o -> o.getClass().getName()));

        networkMessagesService = new NetworkMessagesService(messagesList);

        beansService.bind(networkMessagesService);
    }

    @Async
    @KeepTime
    public void bindMessages() {
        networkMessagesService.bindMessages(false);
    }

    @Async
    @KeepTime
    public void bindMessagesWithDirectionReverse() {
        networkMessagesService.bindMessages(true);
    }

    @Async
    @KeepTime
    public void bindMessageListeners() {
        handlerList.bindHandlers();
    }

    @KeepTime
    public void register(Object handler) {
        handlerList.bind(handler);
    }

    @Synchronized
    public void pull(@NotNull InboundMessageContext<?> context) {
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
