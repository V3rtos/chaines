package me.moonways.endpoint.mojang;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.api.inject.Autobind;
import me.moonways.bridgenet.api.inject.Inject;
import me.moonways.bridgenet.api.inject.PostConstruct;
import me.moonways.endpoint.mojang.http.MojangApiException;
import me.moonways.endpoint.mojang.http.MojangRestApi;
import me.moonways.endpoint.mojang.http.dto.NameAndUuid;
import me.moonways.endpoint.mojang.http.dto.Profile;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Класс MojangApi предоставляет методы для
 * взаимодействия с API Mojang и кеширования
 * результатов запросов.
 */
@Log4j2
@Autobind
public final class MojangApi {

    private MojangRestApi mojangRestApi;

    private final Cache<String, String> idByNicksCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS).build();
    private final Cache<String, String> nicksByIdCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS).build();
    private final Cache<String, Profile> profilesCache = CacheBuilder.newBuilder()
            .expireAfterAccess(1, TimeUnit.DAYS).build();

    @Inject
    private Gson gson;

    /**
     * Инициализирует MojangRestApi и настраивает конечные точки API.
     */
    @PostConstruct
    private void init() {
        mojangRestApi = new MojangRestApi(gson);
        mojangRestApi.mappingEndpoints();
    }

    /**
     * Получает UUID пользователя по его никнейму.
     *
     * @param nickname никнейм пользователя в Minecraft.
     * @return Optional, содержащий UUID пользователя, если он найден.
     */
    public Optional<String> getId(String nickname) {
        idByNicksCache.cleanUp();
        String id = idByNicksCache.getIfPresent(nickname.toLowerCase());

        if (id == null) {
            try {
                NameAndUuid nameAndUuid = mojangRestApi.executeNameAndUuid(nickname);
                id = nameAndUuid.getId();

            } catch (MojangApiException exception) {
                log.debug("§4MojangApi.getId(nickname={}): {}", nickname, exception.toString());
            }

            if (id != null) {
                idByNicksCache.put(nickname.toLowerCase(), id);
            }
        }

        return Optional.ofNullable(id);
    }

    /**
     * Получает профиль пользователя по его UUID.
     *
     * @param id UUID пользователя в Minecraft.
     * @return Optional, содержащий профиль пользователя, если он найден.
     */
    public Optional<Profile> getProfile(String id) {
        profilesCache.cleanUp();
        Profile profile = profilesCache.getIfPresent(id);

        if (profile == null) {
            try {
                profile = mojangRestApi.executeProfile(id);

            } catch (MojangApiException exception) {
                log.debug("§4MojangApi.getProfile(id={}): {}", id, exception.toString());
            }

            if (profile != null) {
                profilesCache.put(id, profile);
                nicksByIdCache.put(id, profile.getName());
            }
        }

        return Optional.ofNullable(profile);
    }

    /**
     * Получает никнейм пользователя по его UUID.
     *
     * @param id UUID пользователя в Minecraft.
     * @return Optional, содержащий никнейм пользователя, если он найден.
     */
    public Optional<String> getNick(String id) {
        nicksByIdCache.cleanUp();
        String nickname = nicksByIdCache.getIfPresent(id);

        if (nickname == null) {
            nickname = getProfile(id).map(Profile::getName)
                    .orElse(null);
        }

        return Optional.ofNullable(nickname);
    }
}
