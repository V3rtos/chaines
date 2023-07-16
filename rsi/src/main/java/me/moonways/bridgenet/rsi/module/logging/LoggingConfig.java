package me.moonways.bridgenet.rsi.module.logging;

import lombok.*;
import me.moonways.bridgenet.rsi.module.ModuleConfiguration;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoggingConfig implements ModuleConfiguration {

    private String poolSize;
}
