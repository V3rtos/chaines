package me.moonways.api.inject;

import lombok.Getter;
import me.moonways.bridgenet.api.inject.Component;
import me.moonways.bridgenet.api.inject.Inject;

@Getter
@Component
public class DecoratedObjectStorage {

  @Inject
  private TestDecoratedObject testProxiedObject;
}
