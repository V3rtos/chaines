package me.moonways.bridgenet.mtp;

import lombok.Getter;
import lombok.Synchronized;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.bean.service.BeansService;
import me.moonways.bridgenet.api.inject.decorator.persistence.Async;
import me.moonways.bridgenet.api.inject.decorator.persistence.KeepTime;
import me.moonways.bridgenet.api.inject.decorator.persistence.RequiredNotNull;
import me.moonways.bridgenet.api.inject.decorator.EnableDecorators;
import me.moonways.bridgenet.api.inject.processor.TypeAnnotationProcessorResult;
import me.moonways.bridgenet.api.inject.processor.persistence.GetTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.processor.persistence.WaitTypeAnnotationProcessor;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.mtp.message.*;
import me.moonways.bridgenet.api.inject.Inject;
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
public class MTPDriver implements Serializable {

    private MessageRegistry messageRegistry;

    @Inject
    private BeansService beansService;
    @Inject
    private MessageHandlerList handlerList;
    @Inject
    private ResponsibleMessageService responsibleMessageService;

    @GetTypeAnnotationProcessor
    private TypeAnnotationProcessorResult<Object> messagesResult;

    @PostConstruct
    void init() {
        List<Object> messagesList = messagesResult.toList();
        messagesList.sort(Comparator.comparing(o -> o.getClass().getSimpleName()));

        messageRegistry = new MessageRegistry(messagesList);

        beansService.bind(messageRegistry);
    }

    @Async
    @KeepTime
    public void bindMessages() {
        messageRegistry.bindMessages(false);
    }

    @Async
    @KeepTime
    public void bindMessagesWithDirectionReverse() {
        messageRegistry.bindMessages(true);
    }

    @RequiredNotNull(message = "message wrapper")
    public MessageWrapper lookupWrapperByID(int id) {
        return messageRegistry.lookupWrapperByID(id);
    }

    @RequiredNotNull(message = "message wrapper")
    public MessageWrapper lookupWrapperByClass(@NotNull Class<?> messageClass) {
        return messageRegistry.lookupWrapperByClass(messageClass);
    }

    @Async
    @KeepTime
    public void bindHandlers() {
        handlerList.bindHandlers();
    }

    @KeepTime
    public void bindHandler(Object handler) {
        handlerList.bind(handler);
    }

    @KeepTime
    @Synchronized
    public void handle(@NotNull InputMessageContext<?> context) {
        handlerList.handle(context);

        Object message = context.getMessage();

        if (responsibleMessageService.isWaiting(message.getClass())) {
            responsibleMessageService.complete(message);
        }
    }
}
