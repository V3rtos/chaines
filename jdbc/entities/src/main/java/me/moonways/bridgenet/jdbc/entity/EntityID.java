package me.moonways.bridgenet.jdbc.entity;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityID {

    public static final EntityID NOT_FOUND = fromId(-1);
    public static final EntityID NOT_AUTO_GENERATED = fromId(-2);

    public static EntityID fromId(long id) {
        return new EntityID(id);
    }

    private final long id;

    public boolean isIncorrect() {
        return id <= 0;
    }

    public boolean isNotFound() {
        return id == NOT_FOUND.id;
    }

    public boolean isNotGenerated() {
        return id == NOT_AUTO_GENERATED.id;
    }
}
