package me.moonways.bridgenet.mtp.config.descriptor;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkSettingsDescriptor {

    private final String host;

    private final int port;

    private final NetworkWorkersDescriptor workers;
    private final NetworkCipherSecurityDescriptor security;
}
