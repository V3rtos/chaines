package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.OrderDirection;

@ToString
@Builder(toBuilder = true)
public class WrappedOrder implements WrappedElement {

    private final OrderDirection direction;
    private final String label;
}
