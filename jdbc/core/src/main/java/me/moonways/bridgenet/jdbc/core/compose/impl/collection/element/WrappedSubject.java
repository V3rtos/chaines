package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.SubjectFunction;

@ToString
@Builder(toBuilder = true)
public class WrappedSubject implements WrappedElement {

    private final SubjectFunction function;
    private final String label;
    private final String association;
}
