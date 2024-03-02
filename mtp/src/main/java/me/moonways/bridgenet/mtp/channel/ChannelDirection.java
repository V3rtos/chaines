package me.moonways.bridgenet.mtp.channel;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.message.persistence.ClientMessage;
import me.moonways.bridgenet.mtp.message.persistence.ServerMessage;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public enum ChannelDirection {

    TO_CLIENT(ServerMessage.class),
    TO_SERVER(ClientMessage.class),
    ;

    private final Class<? extends Annotation> marker;

    public ChannelDirection reverse() {
        switch (this) {
            case TO_CLIENT: return TO_SERVER;
            case TO_SERVER: return TO_CLIENT;
        }
        return null;
    }

    public static ChannelDirection fromAnnotationMarker(Class<? extends Annotation> annotation) {
        for (ChannelDirection direction : values()) {
            if (direction.marker.equals(annotation)) {
                return direction;
            }
        }

        throw new IllegalArgumentException(annotation.getName());
    }
}
