package me.moonways.bridgenet.rest4j;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import me.moonways.bridgenet.rest.client.HttpClient;
import me.moonways.bridgenet.rest.client.HttpClients;
import me.moonways.bridgenet.rest.model.Attributes;
import me.moonways.bridgenet.rest.model.Headers;
import me.moonways.bridgenet.rest.model.authentication.Authentication;
import me.moonways.bridgenet.rest4j.data.*;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class Bridgenet4jRestClient {

    public static Bridgenet4jRestClient create(String baseurl, String apiKey) {
        return new Bridgenet4jRestClient(Bridgenet4jConfig.builder()
                .baseurl(baseurl)
                .apiKey(apiKey)
                .build());
    }

    private final Bridgenet4jConfig config;
    private HttpClient httpClient;

    public Response<OkTotalOnline> getTotalOnline() {
        return executeGet(OkTotalOnline.class, Api.playersPath("totalOnline"));
    }

    public Response<OkPlayerStatus> getPlayerStatus(String playerName) {
        return executeGet(OkPlayerStatus.class, Api.playersPath("status"),
                Attributes.newAttributes().with("name", playerName));
    }

    public Response<OkPermissionGroup> getPlayerGroup(String playerName) {
        return executeGet(OkPermissionGroup.class, Api.permissionsPath("player"),
                Attributes.newAttributes().with("name", playerName));
    }

    public Response<OkServersList> getServersList() {
        return executeGet(OkServersList.class, Api.serversPath("list"));
    }

    public Response<OkServer> getServer(String name) {
        return executeGet(OkServer.class, Api.serversPath("server"),
                Attributes.newAttributes()
                        .with("name", name));
    }

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
