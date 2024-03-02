package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.MergeDirection;

@ToString
@Builder(toBuilder = true)
public class WrappedJoiner implements WrappedElement {

    private final String table;

    private final String sourceLabel;
    private final String targetLabel;

    private final MergeDirection lr_direction;
    private final MergeDirection io_direction;
}
