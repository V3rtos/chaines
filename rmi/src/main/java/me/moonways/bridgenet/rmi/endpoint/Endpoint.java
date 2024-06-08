package me.moonways.bridgenet.rmi.endpoint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import me.moonways.bridgenet.rmi.service.ServiceInfo;

import java.nio.file.Path;

@Getter
@ToString
@RequiredArgsConstructor
public class Endpoint {

    private final ServiceInfo serviceInfo;
    private final Path path;

    private final EndpointConfig config;
}
