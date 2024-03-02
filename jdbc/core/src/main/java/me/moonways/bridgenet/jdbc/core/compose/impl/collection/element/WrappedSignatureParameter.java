package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ParameterType;

@ToString
@Builder(toBuilder = true)
public class WrappedSignatureParameter implements WrappedElement {

    private final String label;
    private final ParameterType type;
    private final Number length;

    private final String encoding;
    private final String encodingCollate;

    private final String addonsLabel;

    private final String defaultValue;
}
