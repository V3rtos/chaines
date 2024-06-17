package me.moonways.endpoint.mojang.http;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import me.moonways.bridgenet.assembly.util.StreamToStringUtils;
import me.moonways.endpoint.mojang.http.dto.NameAndUuid;
import me.moonways.endpoint.mojang.http.dto.Profile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Класс MojangRestApi предоставляет методы для
 * взаимодействия с REST API Mojang.
 */
@Log4j2
@RequiredArgsConstructor
public final class MojangRestApi {

    private static final String USER_AGENT = "BridgeNet-Mojang";
    private static final String CONTENT_TYPE = "application/json";

    private static final String MOJANG_API_URL = "https://api.mojang.com";
    private static final String MOJANG_SESSION_SERVER_URL = "https://sessionserver.mojang.com";
    private static final String PATH__GET_USER_ID = "/users/profiles/minecraft/";
    private static final String PATH__GET_PROFILE = "/session/minecraft/profile/";

    private final Gson gson;
    private final Map<MojangEndpoints, String> endpointsUrlsMap = new HashMap<>();

    /**
     * Создает сопоставление конечных точек API с их URL.
     */
    public void mappingEndpoints() {
        if (!endpointsUrlsMap.isEmpty()) {
            return;
        }
        endpointsUrlsMap.put(MojangEndpoints.MOJANG_API, MOJANG_API_URL);
        endpointsUrlsMap.put(MojangEndpoints.SESSION_SERVER, MOJANG_SESSION_SERVER_URL);
    }

    /**
     * Выполняет запрос к API для получения UUID и имени пользователя по его username.
     *
     * @param username имя пользователя в Minecraft.
     * @return объект NameAndUuid, содержащий имя и UUID пользователя.
     * @throws MojangApiException если произошла ошибка при выполнении запроса.
     */
    public NameAndUuid executeNameAndUuid(String username) throws MojangApiException {
        return executeGet(MojangEndpoints.MOJANG_API, PATH__GET_USER_ID + username, NameAndUuid.class);
    }

    /**
     * Выполняет запрос к API для получения профиля пользователя по его ID.
     *
     * @param id идентификатор пользователя в Minecraft.
     * @return объект Profile, содержащий данные профиля пользователя.
     * @throws MojangApiException если произошла ошибка при выполнении запроса.
     */
    public Profile executeProfile(String id) throws MojangApiException {
        return executeGet(MojangEndpoints.SESSION_SERVER, PATH__GET_PROFILE + id + "?unsigned=false", Profile.class);
    }

    /**
     * Выполняет GET-запрос к указанной конечной точке API и возвращает результат.
     *
     * @param <T> тип возвращаемого объекта.
     * @param endpoint конечная точка API.
     * @param context контекст запроса.
     * @param responseClass класс возвращаемого объекта.
     * @return объект типа T, содержащий результат запроса.
     * @throws MojangApiException если произошла ошибка при выполнении запроса.
     */
    private <T> T executeGet(MojangEndpoints endpoint, String context, Class<T> responseClass) throws MojangApiException {
        String response = doExecute("GET", endpoint, context);
        return gson.fromJson(response, responseClass);
    }

    /**
     * Выполняет HTTP-запрос к указанной конечной точке API и возвращает ответ в виде строки.
     *
     * @param method HTTP-метод (GET, POST и т.д.).
     * @param endpoint конечная точка API.
     * @param context контекст запроса.
     * @return строка, содержащая ответ от сервера.
     * @throws MojangApiException если произошла ошибка при выполнении запроса.
     */
    private String doExecute(String method, MojangEndpoints endpoint, String context) throws MojangApiException {
        if (!context.startsWith("/")) context = "/" + context;

        String url = endpointsUrlsMap.get(endpoint) + context;

        log.debug("{} §7{}", method, url);
        HttpURLConnection connection = connect(url);
        try {
            setHeaders(connection);
            connection.setRequestMethod(method);

            return StreamToStringUtils.toStringFull(connection.getInputStream());

        } catch (IOException exception) {
            throw new MojangApiException(exception);
        } finally {
            connection.disconnect();
        }
    }

    /**
     * Устанавливает соединение с указанной конечной точкой API.
     *
     * @param url URL для запроса.
     * @return HttpURLConnection объект, представляющий соединение.
     * @throws MojangApiException если произошла ошибка при установлении соединения.
     */
    private HttpURLConnection connect(String url) throws MojangApiException {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

            connection.setDoInput(true);
            connection.setReadTimeout(700);
            connection.setConnectTimeout(3000);
            return connection;
        } catch (IOException exception) {
            throw new MojangApiException(exception);
        }
    }

    /**
     * Устанавливает заголовки HTTP-запроса.
     *
     * @param connection объект HttpURLConnection, для которого устанавливаются заголовки.
     */
    private void setHeaders(HttpURLConnection connection) {
        connection.setRequestProperty("Content-Type", CONTENT_TYPE);
        connection.setRequestProperty("User-Agent", USER_AGENT);
    }
}
