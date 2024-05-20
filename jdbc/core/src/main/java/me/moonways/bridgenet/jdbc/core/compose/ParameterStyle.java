package me.moonways.bridgenet.jdbc.core.compose;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.element.Encoding;

import java.util.List;

@Getter
@ToString
@Builder(toBuilder = true)
public class ParameterStyle {

    private final ParameterType type;
    private final Integer length;

    private final Encoding encoding;

    private final List<ParameterSignature> addons;

    private final Object defaultValue;
}
