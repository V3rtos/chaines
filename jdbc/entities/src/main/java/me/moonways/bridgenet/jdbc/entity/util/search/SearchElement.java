package me.moonways.bridgenet.jdbc.entity.util.search;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import me.moonways.bridgenet.jdbc.core.compose.ConditionBinder;
import me.moonways.bridgenet.jdbc.core.compose.ConditionMatcher;

@Getter
@ToString
@Builder(toBuilder = true)
public class SearchElement<T> {

    private final ConditionMatcher matcher;
    private final ConditionBinder binder;
    private final T expectation;
}
