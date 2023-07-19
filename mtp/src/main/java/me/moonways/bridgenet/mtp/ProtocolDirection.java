package me.moonways.bridgenet.mtp;

import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.mtp.exception.DirectionNotFoundException;
import me.moonways.bridgenet.mtp.message.inject.ClientMessage;
import me.moonways.bridgenet.mtp.message.inject.ServerMessage;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public enum ProtocolDirection {

    TO_CLIENT(ClientMessage.class),
    TO_SERVER(ServerMessage.class),
    ;

    private final Class<? extends Annotation> marker;

    public static ProtocolDirection fromAnnotationMarker(Class<? extends Annotation> annotation) {
        for (ProtocolDirection direction : values()) {
            if (direction.marker.equals(annotation)) {
                return direction;
            }
        }

        throw new DirectionNotFoundException(annotation.getName());
    }
}
