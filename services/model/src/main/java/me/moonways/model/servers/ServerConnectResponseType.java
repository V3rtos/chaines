package me.moonways.model.servers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ServerConnectResponseType {

    SUCCESSFUL_CONNECTED(100),
    ALREADY_CONNECTED(200),
    SERVER_NOT_FOUND(300),
    BAD_RESPONSE(400);

    private final int identifier;

    public static ServerConnectResponseType of(int identifier) {
        for (ServerConnectResponseType serverConnectResponseType : ServerConnectResponseType.values()) {
            if (serverConnectResponseType.getIdentifier() == identifier) {
                return serverConnectResponseType;
            }
        }

        return BAD_RESPONSE;
    }
}
