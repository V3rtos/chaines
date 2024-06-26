package me.moonways.bridgenet.rest.client;

import me.moonways.bridgenet.rest.model.BridgenetRestException;
import lombok.experimental.StandardException;

@StandardException
public class HttpClientException extends BridgenetRestException {
    private static final long serialVersionUID = 440060913725454137L;
}
