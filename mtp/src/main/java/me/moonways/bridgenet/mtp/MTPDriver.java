package me.moonways.bridgenet.mtp;

import lombok.Getter;
import lombok.Synchronized;
import me.moonways.bridgenet.api.inject.decorator.definition.Async;
import me.moonways.bridgenet.api.inject.decorator.definition.KeepTime;
import me.moonways.bridgenet.api.inject.decorator.definition.RequiredNotNull;
import me.moonways.bridgenet.api.inject.decorator.DecoratedObject;
import me.moonways.bridgenet.api.proxy.AnnotationInterceptor;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.PostFactoryMethod;
import me.moonways.bridgenet.mtp.message.ExportedMessage;
import me.moonways.bridgenet.mtp.message.MessageRegistry;
import me.moonways.bridgenet.mtp.message.MessageHandlerList;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.message.MessageWrapper;
import org.jetbrains.annotations.NotNull;

@Getter
@Component
@DecoratedObject
public class MTPDriver {

    private final MessageRegistry messages = new MessageRegistry();
    private final MessageHandlerList handlerList = new MessageHandlerList();

    @Inject
    private DependencyInjection dependencyInjection;

    @Inject
    private AnnotationInterceptor interceptor;

    @PostFactoryMethod
    void init() {
        dependencyInjection.injectFields(messages);
        dependencyInjection.injectFields(handlerList);
    }

    @Async
    @KeepTime
    public void bindMessages() {
        messages.bindMessages();
    }

    public void register(@NotNull Class<?> messageClass) {
        messages.register(messageClass);
    }

    @RequiredNotNull(printStackTrace = false)
    public MessageWrapper lookupWrapperByID(int id) {
        return messages.lookupWrapperByID(id);
    }

    @RequiredNotNull(printStackTrace = false)
    public MessageWrapper lookupWrapperByClass(@NotNull Class<?> messageClass) {
        return messages.lookupWrapperByClass(messageClass);
    }

    @RequiredNotNull(printStackTrace = false)
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
