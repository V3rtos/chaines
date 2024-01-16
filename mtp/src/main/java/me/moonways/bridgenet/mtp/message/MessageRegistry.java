package me.moonways.bridgenet.mtp.message;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.DependencyContainer;
import me.moonways.bridgenet.api.inject.DependencyInjection;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.mtp.ProtocolDirection;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.message.inject.MessageHandler;
import me.moonways.bridgenet.mtp.message.inject.ServerMessage;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Log4j2
public class MessageRegistry {

    private final Set<MessageWrapper> messageWrapperSet = Collections.synchronizedSet(new HashSet<>());

    @Inject
    private DependencyInjection injector;

    private MessageWrapper toWrapper(ProtocolDirection direction, Class<?> messageClass) {
        int messageID = messageWrapperSet.size();
        return new MessageWrapper(messageID, messageClass, direction);
    }

    private MessageWrapper toWrapper(Class<? extends Annotation> annotation, Class<?> messageClass) {
        ProtocolDirection direction = ProtocolDirection.fromAnnotationMarker(annotation);
        return toWrapper(direction, messageClass);
    }

    public void bindMessages() {
        injector.peekAnnotatedMembers(ClientMessage.class).forEach(message -> register(message.getClass()));
        injector.peekAnnotatedMembers(ServerMessage.class).forEach(message -> register(message.getClass()));
    }

    private Class<? extends Annotation> findMessageAnnotation(Class<?> messageClass) {
        Class<ClientMessage> clientAnnotation = ClientMessage.class;
        Class<ServerMessage> serverAnnotation = ServerMessage.class;

        if (messageClass.isAnnotationPresent(clientAnnotation)) {
            return clientAnnotation;
        }
        else if (messageClass.isAnnotationPresent(serverAnnotation)) {
            return serverAnnotation;
        }

        return null;
    }

    public void register(Class<?> messageClass) {
        Class<? extends Annotation> messageAnnotation = findMessageAnnotation(messageClass);

        if (messageAnnotation == null) {
            log.info("§4Protocol cannot be register message {}: §cMessage annotation is not found", messageClass.getSimpleName());
            return;
        }

        messageWrapperSet.add(toWrapper(messageAnnotation, messageClass));
        log.info("Protocol was registered message: {}", messageClass.getSimpleName());
    }

    public MessageWrapper lookupWrapperByID(int id) {
        for (MessageWrapper wrapper : messageWrapperSet) {

            if (wrapper.getId() == id) {
                return wrapper;
            }
        }
        return null;
    }

    public MessageWrapper lookupWrapperByClass(@NotNull Class<?> messageClass) {
        for (MessageWrapper wrapper : messageWrapperSet) {

            if (wrapper.matchesSimilar(messageClass)) {
                return wrapper;
            }
        }
        return null;
    }

    public ExportedMessage export(@NotNull Object message) {
        MessageWrapper wrapper = lookupWrapperByClass(message.getClass());
        return new ExportedMessage(wrapper, message);
    }
}
