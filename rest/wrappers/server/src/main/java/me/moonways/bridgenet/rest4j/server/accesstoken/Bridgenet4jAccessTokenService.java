package me.moonways.bridgenet.rest4j.server.accesstoken;

import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.bridgenet.jdbc.entity.EntityRepository;
import me.moonways.bridgenet.jdbc.entity.EntityRepositoryFactory;
import me.moonways.bridgenet.jdbc.entity.Mono;
import me.moonways.bridgenet.rest.server.authentication.token.TokenGenerator;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class Bridgenet4jAccessTokenService {

    private final Map<String, AccessToken> accessTokens = new ConcurrentHashMap<>();
    private EntityRepository<EntityAccessToken> dbRepository;

    @Inject
    private EntityRepositoryFactory entityRepositoryFactory;

    @PostConstruct
    private void init() {
        dbRepository = entityRepositoryFactory.fromEntityType(EntityAccessToken.class);
    }

    public AccessToken grantAccessToken(AccessTokenSource source) {
        return addAccessToken(source,
                TokenGenerator.defaults().generate());
    }

    public AccessToken addAccessToken(AccessTokenSource source, String token) {
        AccessToken accessToken = accessTokens.get(token);

        if (accessToken != null) {
            return accessToken;
        }

        accessToken = AccessToken.builder()
                .token(token)
                .source(source)
                .storeTimeMillis(System.currentTimeMillis())
                .build();

        insertAccessToken(accessToken);
        return accessToken;
    }

    public boolean hasAccessToken(String token) {
        AccessToken accessToken = accessTokens.get(token);
        if (accessToken == null) {
            Optional<EntityAccessToken> entityAccessTokenOptional = findAccessToken(token)
                    .blockOptional();

            if (!entityAccessTokenOptional.isPresent() || entityAccessTokenOptional.get().getId() <= 0) {
                return false;
            }

            EntityAccessToken entityAccessToken = entityAccessTokenOptional.get();
            accessToken = AccessToken.fromEntity(entityAccessToken);

            if (accessToken.isExpired()) {
                deleteExpiredLike(accessToken);
                return false;
            }

            accessTokens.put(entityAccessToken.getToken(), accessToken);
        }
        return true;
    }

    private Mono<EntityAccessToken> findAccessToken(String token) {
        return dbRepository.searchFirst(
                        dbRepository.beginCriteria()
                                .andEquals("token", token));
    }

    private void deleteExpiredLike(AccessToken accessToken) {
        dbRepository.delete(
                dbRepository.beginCriteria()
                        .andEquals("source", AccessTokenSource.CLIENT.ordinal())
                        .andLessOrEquals("date", accessToken.getStoreTimeMillis()));
    }

    private void insertAccessToken(AccessToken accessToken) {
        dbRepository.searchFirst(
                        dbRepository.beginCriteria()
                                .andEquals("source", accessToken.getSource().ordinal())
                                .andEquals("token", accessToken.getToken()))
                .subscribe((entity, ex) -> {
                    if (entity == null) {
                        dbRepository.insert(accessToken.toEntity());
                    }
                    accessTokens.put(accessToken.getToken(), accessToken);
                });
    }
}
