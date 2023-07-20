package me.moonways.bridgenet.mtp.config;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class CipherSecurity {

    private final String salt;
    private final String code;
}
