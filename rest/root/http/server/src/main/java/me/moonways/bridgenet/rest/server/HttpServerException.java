package me.moonways.bridgenet.rest.server;

import me.moonways.bridgenet.rest.model.BridgenetRestException;
import lombok.experimental.StandardException;

@StandardException
public class HttpServerException extends BridgenetRestException {
    private static final long serialVersionUID = 8332698999054089469L;
}
