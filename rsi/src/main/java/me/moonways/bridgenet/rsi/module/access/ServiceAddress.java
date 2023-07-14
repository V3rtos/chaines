package me.moonways.bridgenet.rsi.module.access;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public class ServiceAddress {

    private final String contextUri;

    private final String host;

    private final int port;
}
