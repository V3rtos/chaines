package me.moonways.bridgenet.rest.server.authentication;

import me.moonways.bridgenet.rest.model.BridgenetRestException;
import lombok.experimental.StandardException;

@StandardException
public class HttpAuthorizationException extends BridgenetRestException {
    private static final long serialVersionUID = 8040353704446485915L;
}
