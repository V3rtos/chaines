package me.moonways.bridgenet.rmi.module.access;

import lombok.*;
import me.moonways.bridgenet.rmi.module.ModuleConfiguration;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AccessConfig implements ModuleConfiguration {

    private String remoteHost;
}
