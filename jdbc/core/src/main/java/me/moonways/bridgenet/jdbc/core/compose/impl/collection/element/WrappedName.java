package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;

@ToString
@Builder(toBuilder = true)
public class WrappedName implements WrappedElement {

    private final String value;
}
