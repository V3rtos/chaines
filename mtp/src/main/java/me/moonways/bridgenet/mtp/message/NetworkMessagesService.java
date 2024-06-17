package me.moonways.bridgenet.mtp.message;

import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.api.inject.processor.ScanningResult;
import me.moonways.bridgenet.api.inject.processor.persistence.GetAnnotationsScanningResult;
import me.moonways.bridgenet.api.inject.processor.persistence.AwaitAnnotationsScanning;
import me.moonways.bridgenet.mtp.channel.ChannelDirection;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;
import org.jetbrains.annotations.NotNull;

import java.lang.annotation.Annotation;
import java.util.*;

@Log4j2
@Autobind
@AwaitAnnotationsScanning({ClientMessage.class, ServerMessage.class})
public class NetworkMessagesService {

    private final Set<WrappedNetworkMessage> networkMessageWrappers = Collections.synchronizedSet(new HashSet<>());
    private List<Object> messagesList;

    @GetAnnotationsScanningResult
    private ScanningResult<Object> messagesResult;

    @PostConstruct
    private void init() {
        messagesList = messagesResult.toList();
        messagesList.sort(Comparator.comparing(o -> o.getClass().getName()));
    }

    private WrappedNetworkMessage toWrapper(ChannelDirection direction, Class<?> messageClass) {
        int messageID = networkMessageWrappers.size();
        return new WrappedNetworkMessage(messageID, messageClass, direction);
    }

    private WrappedNetworkMessage toWrapper(Class<? extends Annotation> annotation, Class<?> messageClass) {
        ChannelDirection direction = ChannelDirection.fromAnnotationMarker(annotation);
        return toWrapper(direction, messageClass);
    }

    public void bindMessages(boolean reverse) {
        for (Object message : messagesList) {
            register(reverse, message.getClass());
        }
    }

    private Class<? extends Annotation> findMessageAnnotation(Class<?> messageClass) {
        Class<ClientMessage> clientAnnotation = ClientMessage.class;
        Class<ServerMessage> serverAnnotation = ServerMessage.class;

        if (messageClass.isAnnotationPresent(clientAnnotation)) {
            return clientAnnotation;
        } else if (messageClass.isAnnotationPresent(serverAnnotation)) {
            return serverAnnotation;
        }

        return null;
    }

    public void register(boolean reverse, Class<?> messageClass) {
        Class<ClientMessage> clientAnnotation = ClientMessage.class;
        Class<ServerMessage> serverAnnotation = ServerMessage.class;

        if (messageClass.isAnnotationPresent(clientAnnotation)) {
            registerAnnotated(reverse ? serverAnnotation : clientAnnotation, messageClass);
        }
        if (messageClass.isAnnotationPresent(serverAnnotation)) {
            registerAnnotated(reverse ? clientAnnotation : serverAnnotation, messageClass);
        }
    }

    private void registerAnnotated(Class<? extends Annotation> annotation, Class<?> messageType) {
        WrappedNetworkMessage wrapper = toWrapper(annotation, messageType);
        networkMessageWrappers.add(wrapper);

        log.debug("Protocol was registered message: §3{} §7(id: {}, direction: {})", messageType.getName(),
                wrapper.getId(),
                wrapper.getDirection());
    }

    public WrappedNetworkMessage lookupWrapperByID(int id) {
        for (WrappedNetworkMessage wrapper : networkMessageWrappers) {

            if (wrapper.getId() == id) {
                return wrapper;
            }
        }
        return null;
    }

    public WrappedNetworkMessage lookupWrapperByClass(@NotNull Class<?> messageClass) {
        for (WrappedNetworkMessage wrapper : networkMessageWrappers) {

            if (wrapper.matchesSimilar(messageClass)) {
                return wrapper;
            }
        }
        return null;
    }

    public ExportedMessage export(@NotNull Object message) {
        WrappedNetworkMessage wrapper = lookupWrapperByClass(message.getClass());
        return new ExportedMessage(wrapper, message);
    }
}
