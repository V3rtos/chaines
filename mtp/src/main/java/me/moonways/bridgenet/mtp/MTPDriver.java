package me.moonways.bridgenet.mtp;

import lombok.Getter;
import lombok.Synchronized;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.decorator.persistance.Async;
import me.moonways.bridgenet.api.inject.decorator.persistance.KeepTime;
import me.moonways.bridgenet.api.inject.decorator.persistance.RequiredNotNull;
import me.moonways.bridgenet.api.inject.decorator.Decorated;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.MessageHandlerList;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import org.jetbrains.annotations.NotNull;

@Getter
@Autobind
@Decorated
public class MTPDriver {

    private final MessageRegistry messages = new MessageRegistry();
    private final MessageHandlerList handlerList = new MessageHandlerList();

    @Inject
    private DependencyInjection injector;

    @Inject
    private AnnotationInterceptor interceptor;

    @PostConstruct
    void init() {
        injector.injectFields(messages);
        injector.injectFields(handlerList);
    }

    @Async
    @KeepTime
    public void bindMessages() {
        messages.bindMessages();
    }

    public void register(@NotNull Class<?> messageClass) {
        messages.register(messageClass);
    }

    @RequiredNotNull(message = "message wrapper")
    public MessageWrapper lookupWrapperByID(int id) {
        return messages.lookupWrapperByID(id);
    }

    @RequiredNotNull(message = "message wrapper")
    public MessageWrapper lookupWrapperByClass(@NotNull Class<?> messageClass) {
        return messages.lookupWrapperByClass(messageClass);
    }

    @RequiredNotNull(message = "message export")
    @Synchronized
    public ExportedMessage export(@NotNull Object message) {
        return messages.export(message);
    }

    @Async
    @KeepTime
    public void bindHandlers() {
        handlerList.bindHandlers();
    }

    @KeepTime
    @Synchronized
    public void handle(@NotNull MessageWrapper wrapper, @NotNull Object message) {
        handlerList.handle(wrapper, message);
    }
}
