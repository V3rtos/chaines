package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder(toBuilder = true)
public class Encoding implements WrappedElement {

    private final String characterStyle;
    private final String collate;
}
