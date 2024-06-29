package me.moonways.bridgenet.rest4j;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.client.HttpClient;
import me.moonways.bridgenet.rest.client.HttpClients;
import me.moonways.bridgenet.rest.model.Attributes;
import me.moonways.bridgenet.rest.model.Headers;
import me.moonways.bridgenet.rest.model.authentication.Authentication;
import me.moonways.bridgenet.rest4j.data.*;

/**
 * Класс для работы с API Bridgenet4j.
 * <p>
 * Предоставляет методы для выполнения различных GET-запросов к API.
 * </p>
 *
 * <p>
 * Примеры использования:
 * </p>
 * <pre>{@code
 * Bridgenet4jRestClient client = Bridgenet4jRestClient.create("https://api.example.com", "your-api-key");
 *
 * // Получение общего количества игроков онлайн
 * Response<OkTotalOnline> totalOnlineResponse = client.getTotalOnline();
 * if (totalOnlineResponse.isOk()) {
 *     OkTotalOnline totalOnline = totalOnlineResponse.getOk();
 *     System.out.println("Total online: " + totalOnline.getTotal());
 * }
 *
 * // Получение статуса игрока
 * Response<OkPlayerStatus> playerStatusResponse = client.getPlayerStatus("playerName");
 * if (playerStatusResponse.isOk()) {
 *     OkPlayerStatus playerStatus = playerStatusResponse.getOk();
 *     System.out.println("Player status: " + playerStatus.getStatus());
 * }
 * }</pre>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bridgenet4jRestClient {

    /**
     * Создает новый экземпляр Bridgenet4jRestClient с заданным базовым URL и API-ключом.
     *
     * @param baseurl Базовый URL API.
     * @param apiKey  API-ключ для аутентификации.
     * @return Экземпляр Bridgenet4jRestClient.
     */
    public static Bridgenet4jRestClient create(String baseurl, String apiKey) {
        return new Bridgenet4jRestClient(Bridgenet4jConfig.builder()
                .baseurl(baseurl)
                .apiKey(apiKey)
                .build());
    }

    private final Bridgenet4jConfig config;
    private HttpClient httpClient;

    /**
     * Получает общее количество игроков онлайн.
     *
     * @return Ответ с общим количеством игроков онлайн.
     */
    public Response<OkTotalOnline> getTotalOnline() {
        return executeGet(OkTotalOnline.class, Api.playersPath("totalOnline"));
    }

    /**
     * Получает статус игрока по имени.
     *
     * @param playerName Имя игрока.
     * @return Ответ со статусом игрока.
     */
    public Response<OkPlayerStatus> getPlayerStatus(String playerName) {
        return executeGet(OkPlayerStatus.class, Api.playersPath("status"),
                Attributes.newAttributes().with("name", playerName));
    }

    /**
     * Получает группу прав доступа игрока по имени.
     *
     * @param playerName Имя игрока.
     * @return Ответ с группой прав доступа игрока.
     */
    public Response<OkPermissionGroup> getPlayerGroup(String playerName) {
        return executeGet(OkPermissionGroup.class, Api.permissionsPath("player"),
                Attributes.newAttributes().with("name", playerName));
    }

    /**
     * Получает список серверов.
     *
     * @return Ответ со списком серверов.
     */
    public Response<OkServersList> getServersList() {
        return executeGet(OkServersList.class, Api.serversPath("list"));
    }

    /**
     * Получает информацию о сервере по имени.
     *
     * @param name Имя сервера.
     * @return Ответ с информацией о сервере.
     */
    public Response<OkServer> getServer(String name) {
        return executeGet(OkServer.class, Api.serversPath("server"),
                Attributes.newAttributes()
                        .with("name", name));
    }

    /**
     * Проверяет и инициализирует HttpClient, если он не был инициализирован ранее.
     */
    private void checkClient() {
        if (httpClient == null) {
            httpClient = HttpClients.createSocketClient();
        }
    }

    private <T extends Ok> Response<T> executeGet(Class<T> responseType, String path) {
        return executeGet(responseType, path, Attributes.newAttributes());
    }

    private <T extends Ok> Response<T> executeGet(Class<T> responseType, String path, Attributes attributes) {
        checkClient();
        try {
            Headers headers = createAuthorizedHeaders();
            String url = createUrlString(path);

            return httpClient.executeGet(url, attributes, headers)
                    .map(httpResponse -> ApiResponse.parse(httpResponse, responseType))
                    .orElseGet(() -> ApiResponse.connectionTimedOut("no response content", null));
        } catch (Throwable ex) {
            return ApiResponse.connectionTimedOut("timed out", ex);
        }
    }

    private String createUrlString(String path) {
        return String.format("%s%s", config.getBaseurl(), path);
    }

    private Headers createAuthorizedHeaders() {
        return Headers.newHeaders()
                .set(Headers.Def.AUTHORIZATION,
                        Authentication.BEARER.format(config.getApiKey()));
    }
}
