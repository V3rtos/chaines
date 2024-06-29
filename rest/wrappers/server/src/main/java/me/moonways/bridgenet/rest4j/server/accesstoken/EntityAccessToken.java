package me.moonways.bridgenet.rest4j.server.accesstoken;

import lombok.*;
import me.moonways.bridgenet.jdbc.core.compose.ParameterAddon;
import me.moonways.bridgenet.jdbc.entity.persistence.Entity;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityColumn;
import me.moonways.bridgenet.jdbc.entity.persistence.EntityId;

@Entity(name = "bridgenet4j_accessTokens")
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EntityAccessToken {

    private static final EntityAccessToken UNDEFINED =
            new EntityAccessToken(-1, null, -1, 0);

    public static EntityAccessToken undefined() {
        return UNDEFINED;
    }

    @Getter(onMethod_ = @EntityId)
    private long id;

    @Getter(onMethod_ = @EntityColumn(order = 1,
            indexes = {ParameterAddon.PRIMARY, ParameterAddon.NOTNULL}))
    private final String token;

    @Getter(onMethod_ = @EntityColumn(id = "source", order = 2))
    private final int sourceId;

    @Getter(onMethod_ = @EntityColumn(id = "date", order = 3))
    private final long storeTimeMillis;
}
