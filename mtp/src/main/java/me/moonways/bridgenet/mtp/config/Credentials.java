package me.moonways.bridgenet.mtp.config;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Credentials {

    private final String host;
    private final int port;

    private final CipherSecurity security;
}
