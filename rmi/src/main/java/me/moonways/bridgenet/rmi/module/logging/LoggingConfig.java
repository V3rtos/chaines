package me.moonways.bridgenet.rmi.module.logging;

import lombok.*;
import me.moonways.bridgenet.rmi.module.ModuleConfiguration;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoggingConfig implements ModuleConfiguration {

    private String poolSize;
}
