package me.moonways.bridgenet.rsi.module.access;

import lombok.*;
import me.moonways.bridgenet.rsi.module.ModuleConfiguration;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccessConfig implements ModuleConfiguration {

    private String host;
}
