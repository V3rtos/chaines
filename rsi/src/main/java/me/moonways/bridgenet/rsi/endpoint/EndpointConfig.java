package me.moonways.bridgenet.rsi.endpoint;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
@RequiredArgsConstructor
public class EndpointConfig {

    private final String applicationJarName;
    private final List<String> environments;
}
