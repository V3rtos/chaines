package me.moonways.bridgenet.mtp.config.descriptor;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class NetworkWorkersDescriptor {

    private final int bossThreads;
    private final int childThreads;
}
