package me.moonways.bridgenet.rest4j.server.accesstoken;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.concurrent.TimeUnit;

@Getter
@Builder
@ToString
@EqualsAndHashCode
public class AccessToken {

    private static final long TOKEN_TIMEOUT_MS = TimeUnit.DAYS.toMillis(90);

    public static AccessToken fromEntity(EntityAccessToken entity) {
        return AccessToken.builder()
                .token(entity.getToken())
                .source(AccessTokenSource.values()[entity.getSourceId()])
                .storeTimeMillis(entity.getStoreTimeMillis())
                .build();
    }

    private final String token;
    private final AccessTokenSource source;

    private final long storeTimeMillis;

    public EntityAccessToken toEntity() {
        return new EntityAccessToken(token, source.ordinal(), storeTimeMillis);
    }

    public boolean isExpired() {
        return source == AccessTokenSource.CLIENT &&
                (System.currentTimeMillis() - storeTimeMillis) >= TOKEN_TIMEOUT_MS;
    }
}
