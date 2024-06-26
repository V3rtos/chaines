package me.moonways.bridgenet.rest.server.resource;

import me.moonways.bridgenet.rest.model.BridgenetRestException;
import lombok.experimental.StandardException;

@StandardException
public class HttpServerResourceException extends BridgenetRestException {
    private static final long serialVersionUID = 8228107545499592833L;
}
