package me.moonways.bridgenet.mtp.config;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ExecutorWorkers {

    private final int bossThreads;
    private final int childThreads;
}
