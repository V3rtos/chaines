package me.moonways.bridgenet.assembly.ini.type;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RequiredArgsConstructor
public class IniGroup {

    @EqualsAndHashCode.Include
    private final String key;
    private final IniProperty[] properties;
}
