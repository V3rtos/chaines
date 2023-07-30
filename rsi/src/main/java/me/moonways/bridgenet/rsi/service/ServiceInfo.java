package me.moonways.bridgenet.rsi.service;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class ServiceInfo {

    private final String name;
    private final int port;

    private final Class<? extends RemoteService> modelClass;
}
