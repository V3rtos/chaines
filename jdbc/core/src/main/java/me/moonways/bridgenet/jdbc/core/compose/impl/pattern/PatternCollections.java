package me.moonways.bridgenet.jdbc.core.compose.impl.pattern;

import lombok.*;
import me.moonways.bridgenet.jdbc.core.compose.impl.collection.PatternCollection;
import me.moonways.bridgenet.jdbc.core.util.ObjectPropertyContainer;

@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PatternCollections {

    public static PatternCollections fromPattern(String pattern) {
        return new PatternCollections(pattern);
    }

    private final String pattern;

    private final ObjectPropertyContainer collectionsContainer
            = ObjectPropertyContainer.newWeakContainer()
                .setDefaultKeyModifier(ObjectPropertyContainer.DefaultKeyModifiers.AS_LOWER_CASE);

    public void add(String collectionName, PatternCollection<?> collection) {
        collectionsContainer.setProperty(collectionName, collection);
    }

    public <T> PatternCollection<T> get(String collectionName) {
        return collectionsContainer.getProperty(collectionName);
    }
}
