package me.moonways.service.bnmg.component;

import lombok.*;

import java.util.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public final class BnmgComponentLine {

    private static final String COLLECTION_PREFIX = "[";
    private static final String COLLECTION_SUFFIX = "[";
    private static final String COLLECTION_DELIM = "; ";

    @Setter
    private int index;

    private final String content;

    private void validateCollectionFormat() {
        if (isNotCollection() || !content.contains(COLLECTION_DELIM)) {
            throw new IllegalArgumentException("content type is not collection");
        }
    }

    public boolean isNotCollection() {
        return !content.startsWith(COLLECTION_PREFIX) || !content.endsWith(COLLECTION_SUFFIX);
    }

    public Collection<String> wrapCollection() {
        validateCollectionFormat();

        if (isNotCollection()) {
            return Collections.singletonList(content);
        }

        String[] contentsArray = content.split(COLLECTION_DELIM);
        List<String> unmodifiableList = Arrays.asList(contentsArray);

        return new ArrayList<>(unmodifiableList);
    }
}
