package me.moonways.bridgenet.jdbc.core.compose.impl.collection.element;

import lombok.Builder;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ConditionBinder;
import me.moonways.bridgenet.jdbc.core.compose.ConditionMatcher;

@ToString
@Builder(toBuilder = true)
public class WrappedCondition implements WrappedElement {

    private ConditionMatcher matcher;
    private ConditionBinder binderNext;
    private String label;
    private String value;
}
