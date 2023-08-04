package me.moonways.api.inject;

import lombok.Getter;
import me.moonways.bridgenet.api.inject.Depend;
import me.moonways.bridgenet.api.inject.Inject;

@Getter
@Depend
public class DecoratedObjectStorage {

    @Inject
    private TestDecoratedObject testProxiedObject;
}
